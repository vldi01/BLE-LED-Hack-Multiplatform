package core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

@Suppress("CONFLICTING_OVERLOADS")
actual class Scanner {
    private var cbCentralManager: CBCentralManager? = null

    private val _devices = MutableStateFlow(emptyList<BluetoothDevice>())
    actual val devices: Flow<List<BluetoothDevice>> = _devices.asStateFlow()

    private var isScanning = false

    private val nativeScanner = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            if (central.state == CBManagerStatePoweredOn) {
                central.scanForPeripheralsWithServices(null, null)
            }
        }

        override fun centralManager(
            central: CBCentralManager,
            didDiscoverPeripheral: CBPeripheral,
            advertisementData: Map<Any?, *>,
            RSSI: NSNumber
        ) {
            didDiscoverPeripheral.name ?: return
            _devices.update {
                it + BluetoothDevice(
                    scanner = this@Scanner,
                    peripheral = didDiscoverPeripheral,
                    name = didDiscoverPeripheral.name ?: "",
                    id = didDiscoverPeripheral.identifier.UUIDString,
                )
            }
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            val id = didConnectPeripheral.identifier.UUIDString
            _devices.value.firstOrNull { it.id == id }?.onConnect()
        }

        override fun centralManager(central: CBCentralManager, didDisconnectPeripheral: CBPeripheral, error: NSError?) {
            val id = didDisconnectPeripheral.identifier.UUIDString
            _devices.value.firstOrNull { it.id == id }?.onDisconnect()
        }

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) {
            val id = didFailToConnectPeripheral.identifier.UUIDString
            _devices.value.firstOrNull { it.id == id }?.onFail()
        }
    }


    actual fun startScan() {
        if (isScanning) return

        isScanning = true
        _devices.value = emptyList()
        cbCentralManager = CBCentralManager(nativeScanner, null)
    }

    actual fun stopScan() {
        if (!isScanning) return

        isScanning = false
        cbCentralManager?.stopScan()
    }

    fun connectDevice(peripheral: CBPeripheral) {
        cbCentralManager?.connectPeripheral(peripheral, null)
    }

    fun disconnectDevice(peripheral: CBPeripheral) {
        cbCentralManager?.cancelPeripheralConnection(peripheral)
    }
}