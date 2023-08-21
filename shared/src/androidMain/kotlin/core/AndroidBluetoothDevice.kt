package core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class BluetoothDevice(
    val nativeDevice: android.bluetooth.BluetoothDevice
) {
    private val _state = MutableStateFlow(BluetoothDeviceState.NotConnected)
    actual val state: StateFlow<BluetoothDeviceState> = _state.asStateFlow()

    actual val name: String
        get() = TODO("Not yet implemented")
    actual val id: String
        get() = TODO("Not yet implemented")
    actual val characteristics: Map<String, ServiceCharacteristic>
        get() = TODO("Not yet implemented")

    actual fun connect() {
        TODO("Not yet implemented")
    }

    actual fun disconnect() {
        TODO("Not yet implemented")
    }


    actual suspend fun write(data: ByteArray, characteristicId: String): Boolean {
        TODO("Not yet implemented")
    }

}
