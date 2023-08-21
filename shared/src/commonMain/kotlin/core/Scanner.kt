package core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

expect class Scanner {
    val devices: StateFlow<List<BluetoothDevice>>

    fun startScan()
    fun stopScan()
}