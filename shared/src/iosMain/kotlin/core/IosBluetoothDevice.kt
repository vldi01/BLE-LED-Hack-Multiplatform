package core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class BluetoothDevice actual constructor(
    actual val name: String,
    actual val id: String
) : NSObject(), CBPeripheralDelegateProtocol {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    actual val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    private val _characteristics = mutableListOf<DeviceCharacteristic>()
    actual val characteristics: List<DeviceCharacteristic> get() = _characteristics.toList()

    private var peripheral: CBPeripheral? = null
    private var scanner: Scanner? = null

    private var sendWaitJob: Job? = null
    private val mutex = Mutex()
    private val scope = DI.scope

    constructor(scanner: Scanner, peripheral: CBPeripheral, name: String, id: String) : this(name, id) {
        this.scanner = scanner
        this.peripheral = peripheral
    }

    actual fun connect() {
        if (_state.value == BluetoothDeviceState.Connecting || _state.value == BluetoothDeviceState.Connected) return

        _state.value = BluetoothDeviceState.Connecting
        peripheral?.let {
            it.delegate = this
            scanner?.connectDevice(it)
        }
    }

    actual fun disconnect() {
        if (_state.value != BluetoothDeviceState.Connected && _state.value != BluetoothDeviceState.Connecting) return

        peripheral?.let { scanner?.disconnectDevice(it) }
    }

    fun onConnect() {
        _state.value = BluetoothDeviceState.Connected
        _characteristics.clear()
        peripheral?.discoverServices(null)
    }

    fun onDisconnect() {
        _state.value = BluetoothDeviceState.NotConnected
        _characteristics.clear()
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

    override fun peripheral(peripheral: CBPeripheral, didWriteValueForCharacteristic: CBCharacteristic, error: NSError?) {
        sendWaitJob?.cancel()
        if(error != null) {
            println("error = ${error}")
        }
    }

    override fun peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService: CBService, error: NSError?) {
        val service = didDiscoverCharacteristicsForService

        val deviceCharacteristics = service.characteristics
            ?.mapNotNull { any ->
                (any as? CBCharacteristic)?.let { DeviceCharacteristic(it, peripheral) }
            }
            ?: return

        _characteristics += deviceCharacteristics
    }

    actual suspend fun write(data: ByteArray, characteristicId: String): Boolean {
        mutex.withLock {
            _characteristics.firstOrNull { it.id == characteristicId }?.write(data)
        }

        if (_state.value != BluetoothDeviceState.Connected) return false

        mutex.withLock {
            val characteristic = _characteristics.firstOrNull { it.id == characteristicId } ?: return false
            var timeout = false

            sendWaitJob = scope.launch {
                delay(SEND_TIMEOUT_MILLIS)
                timeout = true
            }

            characteristic.write(data)
            sendWaitJob?.join()

            return !timeout
        }
    }
}

private const val SEND_TIMEOUT_MILLIS = 5000L