package com.dpadninja.iromium.ui.core

import android.view.KeyEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.lang.System.currentTimeMillis

@Composable
fun ColorCircle(
    color: Color,
    size: Dp = 24.dp,
    isActive: Boolean = false,
    saveFocus: Boolean = false,
    focusedBorderWidth: Dp = 2.dp,
    focusedBorderColor: Color = Color(0xccffffff),
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
) {
    var isActive: Boolean = isActive
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    var pressStartTime by remember { mutableStateOf<Long?>(null) }
    var pressDuration by remember { mutableStateOf<Long?>(null) }
    var isPressed by remember { mutableStateOf(false) }

    var isFocusChangeNeeded by remember { mutableStateOf(saveFocus) }

    val density = LocalDensity.current
    val sizePx = with(density) { size.toPx() }

    LaunchedEffect(Unit) {
        if (isActive and isFocusChangeNeeded) {
            focusRequester.requestFocus()
            isFocusChangeNeeded = false
        }
    }

    Canvas(
        modifier =
            Modifier
                .size(size)
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    if (event.key == Key.Enter || event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                        if (pressStartTime == null) {
                            pressStartTime = currentTimeMillis()
                        }
                        when (event.type) {
                            KeyEventType.KeyDown -> {
                                isPressed = true
                                pressStartTime?.let {
                                    pressDuration = currentTimeMillis() - it
                                    if (pressDuration!! > 300) { // 300ms threshold
                                        isActive = true
                                        onClick?.invoke()
                                        onLongPress?.invoke()
                                        pressStartTime = null
                                    }
                                }
                                true
                            }

                            KeyEventType.KeyUp -> {
                                isPressed = false
                                isActive = true
                                onClick?.invoke()
                                pressStartTime?.let {
                                    val duration = currentTimeMillis() - it
                                    if (duration > 300) {
                                        onLongClick?.invoke()
                                    }
                                }
                                pressStartTime = null
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                }.focusable(interactionSource = interactionSource),
    ) {
        val radius = sizePx / 2f
        val center = Offset(radius, radius)

        // Main circle:
        drawCircle(
            color = color,
            center = center,
            radius = radius,
            blendMode = if (isPressed) BlendMode.Difference else DefaultBlendMode,
        )

        // Selected dot:
        if (isActive) {
            drawCircle(
                color = Color(0xcc000000),
                center = center,
                radius = radius / 2f,
                blendMode = if (isPressed) BlendMode.Darken else DefaultBlendMode,
            )
        }
        // Focused:
        if (isFocused) {
            drawCircle(
                color = focusedBorderColor,
                center = center,
                radius = radius + focusedBorderWidth.toPx(),
                style = Stroke(width = focusedBorderWidth.toPx()),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Panel(
    modifier: Modifier = Modifier,
    presets: List<Color>,
    initialIndex: Int = 0,
    saveFocus: Boolean = false,
    onColorClick: (Int) -> Unit = {},
    onColorLongClick: (Int) -> Unit = {},
    onColorLongPress: (Int) -> Unit = {},
    colorsInRow: Int = 6,
) {
    var selectedIndex by remember { mutableIntStateOf(initialIndex) }
    modifier.shadow(elevation = 2.dp)
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        presets.chunked(colorsInRow).forEachIndexed { rowIndex, rowColors ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowColors.forEachIndexed { columnIndex, color ->
                    val i = rowIndex * colorsInRow + columnIndex
                    val isActive = (i == selectedIndex)

                    ColorCircle(
                        color = color,
                        isActive = isActive,
                        saveFocus = saveFocus,
                        onClick = {
                            selectedIndex = i
                            onColorClick(i)
                        },
                        onLongPress = {
                            onColorLongPress(i)
                        },
                    )
                }
            }
        }
    }
}
