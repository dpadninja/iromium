package com.dpadninja.iromium.ui.window

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dpadninja.iromium.ui.core.BasicWindow
import com.dpadninja.iromium.ui.core.QrCode
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel

@Composable
fun QRWindow(
    viewModel: AppViewModel,
) {
    var qrCodeText by remember { mutableStateOf(viewModel.qrCodeText) }
    BasicWindow(Modifier.width(200.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Scan the QR code below",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        QrCode(qrCodeText)
    }
}
