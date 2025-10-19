package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BasicWindow(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .background(windowBackgroundColor, windowCornerShape)
                .padding(10.dp),
        content = content,
    )
}

@Composable
fun Container(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2C2C49),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .background(color, RoundedCornerShape(16.dp)),
        content = content,
    )
}
