import kotlinx.coroutines.flow.StateFlow

interface Scanner {
    val devices: StateFlow<List<Pair<String, BluetoothDevice>>>

    fun startScan() {}
    fun stopScan() {}
}