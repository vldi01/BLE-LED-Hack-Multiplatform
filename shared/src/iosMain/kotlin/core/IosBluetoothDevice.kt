package core

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.create
import platform.darwin.NSObject

actual class BluetoothDevice(
    private val scanner: Scanner,
    private val peripheral: CBPeripheral,
    actual val name: String,
    actual val id: String
) : NSObject(), CBPeripheralDelegateProtocol {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    actual val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    private val _characteristics = hashMapOf<String, ServiceCharacteristic>()
    actual val characteristics: Map<String, ServiceCharacteristic> = _characteristics

    actual fun connect() {
        if (_state.value == BluetoothDeviceState.Connecting || _state.value == BluetoothDeviceState.Connected) return

        _state.value = BluetoothDeviceState.Connecting
        peripheral.delegate = this
        scanner.connectDevice(peripheral)
    }

    actual fun disconnect() {
        if (_state.value != BluetoothDeviceState.Connected && _state.value != BluetoothDeviceState.Connecting) return

        scanner.disconnectDevice(peripheral)
    }

    fun onConnect() {
        _state.value = BluetoothDeviceState.Connected
        peripheral.discoverServices(null)
    }

    fun onDisconnect() {
        _state.value = BluetoothDeviceState.NotConnected
        connect()
    }

    fun onFail() {
        _state.value = BluetoothDeviceState.Failed
        _characteristics.clear()
    }

    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
        if (didDiscoverServices != null) {
            _state.value = BluetoothDeviceState.Failed
            return
        }

        peripheral.services?.forEach {
            val service = (it as? CBService) ?: return@forEach
            peripheral.discoverCharacteristics(null, service)
        }
    }

    override fun peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService: CBService, error: NSError?) {
        val service = didDiscoverCharacteristicsForService

        service.characteristics?.forEach {
            val nativeCharacteristic = (it as? CBCharacteristic) ?: return@forEach
            val characteristic = ServiceCharacteristic(nativeCharacteristic)
            _characteristics[characteristic.id] = characteristic
        }
    }

    @BetaInteropApi
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun write(data: ByteArray, characteristicId: String): Boolean {
        if (_state.value != BluetoothDeviceState.Connected) return false

        val characteristic = _characteristics[characteristicId] ?: return false

        val nsData = memScoped {
            NSData.create(
                bytes = allocArrayOf(data),
                length = data.size.toULong()
            )
        }
        peripheral.writeValue(nsData, characteristic.nativeCharacteristic, CBCharacteristicWriteWithoutResponse)

        return true
    }
}
