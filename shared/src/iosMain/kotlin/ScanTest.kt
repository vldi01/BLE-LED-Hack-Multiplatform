import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBConnectionEvent
import platform.CoreBluetooth.CBManagerState
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject


actual class ScanTest : NSObject(), CBCentralManagerDelegateProtocol {
    private var cbCentralManager: CBCentralManager? = null

    actual fun printScan() {
        cbCentralManager = CBCentralManager(this, null)
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state == CBManagerStatePoweredOn) {
            central.scanForPeripheralsWithServices(null, null)
            println("Scanning...")
        }
    }

    override fun centralManager(
        central: CBCentralManager,
        didDiscoverPeripheral: CBPeripheral,
        advertisementData: Map<Any?, *>,
        RSSI: NSNumber
    ) {
        val name = didDiscoverPeripheral.name ?: return

        println("Discovered name: $name")
    }
}

