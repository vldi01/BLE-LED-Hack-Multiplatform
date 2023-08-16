package core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class BluetoothDevice actual constructor(actual val name: String?) {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    actual val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    private val _characteristics = mutableListOf<DeviceCharacteristic>()
    actual val characteristics: List<DeviceCharacteristic> = _characteristics.toList()

    actual fun connect() {
        TODO("Not yet implemented")
    }

    actual fun disconnect() {
        TODO("Not yet implemented")
    }

}