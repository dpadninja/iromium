package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon

@Composable
fun UIButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: String,
    textColor: Color = com.dpadninja.iromium.ui.core.textColor,
    color: Color = buttonBackgroundColor,
    focusedColor: Color = buttonBackgroundColorFocused,
    onClick: () -> Unit,
    small: Boolean = false,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    val finalContent = content ?: { Text(text) }
    var size = 40.dp
    var contentPadding = contentPadding

    var shape = buttonCornerShape

    if (small) {
        contentPadding = PaddingValues(horizontal = 10.dp)
        size = 30.dp
        shape = RoundedCornerShape(CornerSize(15.dp))
    }

    Button(
        contentPadding = contentPadding,
        modifier =
            modifier
                .height(size)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
        onClick = onClick,
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    when {
                        isFocused -> focusedColor
                        else -> color
                    },
                contentColor = textColor,
            ),
        content = finalContent,
    )
}

@Composable
fun IconUIButton(
    modifier: Modifier = Modifier,
    small: Boolean = false,
    icon: ImageVector,
    text: String,
    color: Color = Color.Transparent,
    focusedColor: Color = buttonBackgroundColorFocused,
    onClick: () -> Unit = {},
) {
    var contentPadding = PaddingValues(0.dp)
    var size = 40.dp
    if (small) {
        contentPadding = PaddingValues(2.dp)
        size = 24.dp
    }
    UIButton(
        modifier = modifier.size(size),
        contentPadding = contentPadding,
        text = text,
        textColor = textColor,
        color = color,
        focusedColor = focusedColor,
        onClick = onClick,
        content = { Icon(icon, contentDescription = text, tint = textColor) },
    )
}
