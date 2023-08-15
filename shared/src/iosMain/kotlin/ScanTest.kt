import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

@Suppress("CONFLICTING_OVERLOADS")
actual class ScanTest : NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {
    private var cbCentralManager: CBCentralManager? = null
    private var device: CBPeripheral? = null

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

        if(name.contains("Triones")) {
            device = didDiscoverPeripheral
            central.stopScan()
            central.connectPeripheral(didDiscoverPeripheral, null)
        }
    }

    override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
        println("Connected: ${didConnectPeripheral.name}")
        didConnectPeripheral.discoverServices(null)
        didConnectPeripheral.delegate = this
    }

    override fun centralManager(
        central: CBCentralManager,
        didDisconnectPeripheral: CBPeripheral,
        error: NSError?
    ) {
        println("Disconnected: ${didDisconnectPeripheral.name}")
    }

    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {

    }
}

