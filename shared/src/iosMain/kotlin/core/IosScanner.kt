package core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOff
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateUnauthorized
import platform.CoreBluetooth.CBManagerStateUnknown
import platform.CoreBluetooth.CBManagerStateUnsupported
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.darwin.NSObject

@Suppress("CONFLICTING_OVERLOADS")
actual class Scanner : NSObject(), CBCentralManagerDelegateProtocol {
    private var cbCentralManager: CBCentralManager? = null

    private val _devices = MutableStateFlow(emptyList<BluetoothDevice>())
    actual val devices: StateFlow<List<BluetoothDevice>> = _devices.asStateFlow()

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
        _devices.update { it + BluetoothDevice(didDiscoverPeripheral.name ?: "", didDiscoverPeripheral.identifier.UUIDString) }
    }

    actual fun startScan() {
        cbCentralManager = CBCentralManager(this, null)
    }

    actual fun stopScan() {
        cbCentralManager?.stopScan()
        cbCentralManager = null
    }
}