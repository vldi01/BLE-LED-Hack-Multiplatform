package core

import platform.CoreBluetooth.CBCharacteristic
import platform.Foundation.setValue

actual class ServiceCharacteristic(characteristic: CBCharacteristic) {
    val nativeCharacteristic: CBCharacteristic = characteristic
    actual val id: String
    init {
        id = characteristic.UUID.UUIDString
    }
}
