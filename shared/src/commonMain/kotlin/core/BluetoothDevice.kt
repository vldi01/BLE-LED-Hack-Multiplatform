package core

import kotlinx.coroutines.flow.StateFlow

enum class BluetoothDeviceState {
    Connected, NotConnected
}

interface BluetoothDevice {
    val state: StateFlow<BluetoothDeviceState>
    val characteristics: List<DeviceCharacteristic>

    fun connect()
    fun disconnect()
}