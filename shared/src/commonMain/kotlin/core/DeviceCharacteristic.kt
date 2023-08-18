package core

expect class DeviceCharacteristic(id: String) {
    val id: String
    fun write(data: ByteArray)
}