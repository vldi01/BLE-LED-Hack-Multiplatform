package architecture

import core.BluetoothDevice

data class AppState(
    val discoveredDevices: List<BluetoothDevice> = emptyList(),
    val isScanning: Boolean = false
)
