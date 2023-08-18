package core

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSData
import platform.Foundation.create

actual class DeviceCharacteristic actual constructor(actual val id: String) {
    private var characteristic: CBCharacteristic? = null
    private var peripheral: CBPeripheral? = null

    constructor(characteristic: CBCharacteristic, peripheral: CBPeripheral) : this(characteristic.UUID.UUIDString) {
        this.characteristic = characteristic
        this.peripheral = peripheral
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun write(data: ByteArray) {
        val ch = characteristic ?: return
        val nsData = memScoped {
            NSData.create(
                bytes = allocArrayOf(data),
                length = data.size.toULong()
            )
        }
        peripheral?.writeValue(nsData, ch, CBCharacteristicWriteWithResponse)
    }

    override fun toString(): String {
        return "DeviceCharacteristic(characteristic=$characteristic, id=$id)"
    }

}