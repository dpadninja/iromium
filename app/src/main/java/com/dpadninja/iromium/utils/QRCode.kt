package com.dpadninja.iromium.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun generateQrCode(
    text: String,
    size: Int = 512,
): Bitmap {
    val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }
    return bmp
}
