package core

import kotlinx.coroutines.flow.StateFlow

enum class BluetoothDeviceState {
    Connected, NotConnected
}

expect class BluetoothDevice(name: String?) {
    val state: StateFlow<BluetoothDeviceState>
    val characteristics: List<DeviceCharacteristic>

    val name: String?

    fun connect()
    fun disconnect()
}