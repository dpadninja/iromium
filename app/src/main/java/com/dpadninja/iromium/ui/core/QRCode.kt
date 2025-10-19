package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.dpadninja.iromium.utils.generateQrCode

@Composable
fun QrCode(
    text: String,
    modifier: Modifier = Modifier,
) {
    val qrBitmap by remember(text) { mutableStateOf(generateQrCode(text)) }
    requireNotNull(qrBitmap) { "qrBitmap is null" }
    Image(
        bitmap = qrBitmap.asImageBitmap(),
        contentDescription = "QR code",
        modifier = modifier.clip(shape = RoundedCornerShape(16.dp)),
    )
}
