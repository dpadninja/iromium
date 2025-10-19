package com.dpadninja.iromium.ble

import android.util.Log


fun powerOffPacket(): ByteArray {
    Log.d("BLE PACKET", "Power Off")
    return byteArrayOf(0x7e, 0x04, 0x04, 0x00, 0x00, 0x00, 0xff.toByte(), 0x00, 0xef.toByte())
}

fun powerOnPacket(): ByteArray {
    Log.d("BLE PACKET", "Power On")
    return byteArrayOf(0x7e, 0x00, 0x04, 0xf0.toByte(), 0x00, 0x01, 0xff.toByte(), 0x00, 0xef.toByte())
}

@OptIn(ExperimentalStdlibApi::class)
fun colorPacket(color: String): ByteArray {
    Log.d("BLE PACKET", "Color: $color")
    val red = color.substring(1, 3).hexToByte()
    val green = color.substring(3, 5).hexToByte()
    val blue = color.substring(5, 7).hexToByte()

    return byteArrayOf(
        0x7e,
        0x00,
        0x05,
        0x03,
        red,
        green,
        blue,
        0x00,
        0xef.toByte(),
    )
}

fun brightnessPacket(brightness: Int): ByteArray {
    Log.d("BLE PACKET", "Brightness: $brightness")
    if (brightness !in 0..100) {
        throw IllegalArgumentException("Brightness must be in range 0..100")
    }
    return byteArrayOf(
        0x7e,
        0x00,
        0x01,
        brightness.toByte(),
        0x00,
        0x00,
        0x00,
        0x00,
        0xef.toByte(),
    )
}
