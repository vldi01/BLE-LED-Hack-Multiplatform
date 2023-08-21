package core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

expect class Scanner {
    val devices: Flow<List<BluetoothDevice>>

    fun startScan()
    fun stopScan()
}