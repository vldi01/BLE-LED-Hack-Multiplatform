import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.darwin.NSObject

class IosScanner : Scanner, NSObject(), CBCentralManagerDelegateProtocol {
    private var cbCentralManager: CBCentralManager? = null

    private val _devices = MutableStateFlow(emptyList<Pair<String, BluetoothDevice>>())
    override val devices: StateFlow<List<Pair<String, BluetoothDevice>>> = _devices.asStateFlow()

    override fun centralManagerDidUpdateState(central: CBCentralManager) {

    }

    override fun startScan() {
        cbCentralManager = CBCentralManager(this, null)
    }

    override fun stopScan() {
        cbCentralManager?.stopScan()
        cbCentralManager = null
    }


}