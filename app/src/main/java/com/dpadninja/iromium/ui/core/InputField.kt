package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UIInternalKeyboardInput(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White,
    value: String = "",
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    fontFamily: FontFamily = FontFamily.Monospace,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .border(
                    BorderStroke(2.dp, borderColor),
                    shape = RoundedCornerShape(16.dp),
                ).background(color = windowBackgroundColor2, shape = RoundedCornerShape(16.dp))
                .padding(10.dp)
                .fillMaxWidth(),
    ) {
        if (value == "") {
            Text(
                text = "Enter MAC address",
                color = Color(0x60FFFFFF),
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
            )
        }
        Text(value, color = textColor, fontSize = fontSize, fontWeight = fontWeight, fontFamily = fontFamily)
    }
}
