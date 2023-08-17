package architecture

import core.BluetoothDevice
import core.BluetoothDeviceState

data class AppState(
    val discoveredDevices: List<BluetoothDevice> = emptyList(),
    val isScanning: Boolean = false,
    val selectedDevice: BluetoothDevice? = null
)
