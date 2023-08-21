package core

import android.bluetooth.BluetoothGattCharacteristic
import androidx.compose.ui.text.toUpperCase
import java.util.Locale

@OptIn(ExperimentalStdlibApi::class)
actual class ServiceCharacteristic(
    val nativeCharacteristic: BluetoothGattCharacteristic
) {
    actual val id: String = nativeCharacteristic.uuid.toString().split("-").first().substring(4).uppercase()
    override fun toString(): String {
        return "ServiceCharacteristic(id='$id')"
    }
}