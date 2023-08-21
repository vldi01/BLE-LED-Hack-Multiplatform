package core

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("MissingPermission")
actual class BluetoothDevice(
    private val nativeDevice: android.bluetooth.BluetoothDevice
): KoinComponent {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    actual val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    actual val name: String = nativeDevice.name ?: ""
    actual val id: String = nativeDevice.address ?: ""

    private val _characteristics = hashMapOf<String, ServiceCharacteristic>()
    actual val characteristics: Map<String, ServiceCharacteristic> = _characteristics

    private val context by inject<Application>()
    private val scope by inject<CoroutineScope>()
    private var sendWaitJob: Job? = null
    private val mutex = Mutex()
    private var gatt: BluetoothGatt? = null

    private val callback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Connected to the device, now discover services.
                gatt?.discoverServices()
            } else {
                scope.launch {
                    _state.emit(BluetoothDeviceState.NotConnected)
                }
            }
        }

        @SuppressLint("MissingPermission", "NewApi")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            gatt?.services?.forEach { service ->
                service.characteristics?.forEach {
                    val characteristic = ServiceCharacteristic(it)

                    _characteristics[characteristic.id] = characteristic
                }
            }
            scope.launch {
                _state.emit(BluetoothDeviceState.Connected)
            }
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            sendWaitJob?.cancel()
        }
    }

    actual fun connect() {
        gatt = nativeDevice.connectGatt(context, true, callback)
        scope.launch {
            _state.emit(BluetoothDeviceState.Connecting)
        }
    }

    actual fun disconnect() {
        gatt?.disconnect()
        scope.launch {
            _state.emit(BluetoothDeviceState.NotConnected)
        }
    }


    actual suspend fun write(data: ByteArray, characteristicId: String): Boolean {
        if (state.value != BluetoothDeviceState.Connected) return false

        mutex.withLock {
            val characteristic = _characteristics[characteristicId] ?: return false
            var timeout = false

            sendWaitJob = scope.launch {
                delay(SEND_TIMEOUT_MILLIS)
                timeout = true
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt?.writeCharacteristic(characteristic.nativeCharacteristic, data, BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) ?: return false
            } else {
                characteristic.nativeCharacteristic.setValue(data)
                gatt?.writeCharacteristic(characteristic.nativeCharacteristic)
            }
            sendWaitJob?.join()

            return !timeout
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BluetoothDevice

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        private const val SEND_TIMEOUT_MILLIS = 5000L
    }
}
