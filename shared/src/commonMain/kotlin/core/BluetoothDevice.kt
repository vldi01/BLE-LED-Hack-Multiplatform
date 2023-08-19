package core

import kotlinx.coroutines.flow.StateFlow

enum class BluetoothDeviceState {
    Connected, NotConnected, Failed, Connecting
}

expect class BluetoothDevice {
    val state: StateFlow<BluetoothDeviceState>
    val characteristics: Map<String, ServiceCharacteristic>

    val name: String
    val id: String

    fun connect()
    fun disconnect()

    suspend fun write(data: ByteArray, characteristicId: String): Boolean
}