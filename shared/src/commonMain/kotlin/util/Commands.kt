package util

import androidx.compose.ui.graphics.Color

object Commands {
    fun colorCommand(
        red: Byte,
        green: Byte,
        blue: Byte,
    ): ByteArray =
        byteArrayOf(0x56, red, green, blue, 0x00, 0xf0.toByte(), 0xaa.toByte())

    fun colorCommand(
        color: Color
    ): ByteArray =
        listOf(0x56, (color.red*0xff).toInt(), (color.green*0xff).toInt(), (color.blue*0xff).toInt(), 0x00, 0xf0, 0xaa)
            .map { it.toByte() }
            .toByteArray()

    fun switchOn(on: Boolean): ByteArray =
        listOf(0xcc, if(on) 0x23 else 0x24, 0x33)
            .map { it.toByte() }
            .toByteArray()
}