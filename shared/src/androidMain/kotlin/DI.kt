import core.BluetoothDevice
import core.Scanner
import kotlinx.coroutines.flow.StateFlow

actual object MDI {
    actual val scanner: Scanner = object : Scanner {
        override val devices: StateFlow<List<Pair<String, BluetoothDevice>>>
            get() = TODO("Not yet implemented")

    }
}