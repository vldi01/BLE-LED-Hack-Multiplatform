package core

import kotlinx.coroutines.flow.StateFlow

actual class Scanner {
    actual val devices: StateFlow<List<BluetoothDevice>>
        get() = TODO("Not yet implemented")

    actual fun startScan() {
    }

    actual fun stopScan() {
    }
}