package core

import platform.CoreBluetooth.CBCharacteristic

actual class ServiceCharacteristic(characteristic: CBCharacteristic) {
    val nativeCharacteristic: CBCharacteristic = characteristic
    actual val id: String

    init {
        id = characteristic.UUID.UUIDString
    }
}
