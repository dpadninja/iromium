package com.dpadninja.iromium.ui.core

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun UISliderHorizontal(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    step: Float = 0.1f,
    thumbColor: Color = Color(0xfffcb935),
    trackColor: Color = Color(0xffb8a73e),
    trackGradientFrom: Color = Color(0x00b8a73e),
    trackGradientTo: Color = Color(0xffb8a73e),
    trackInactiveColor: Color = Color.DarkGray,
    useGradient: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val trackHeight: Dp = 4.dp
    val thumbRadius: Dp = 8.dp
    val focusedBorderColor = Color(0xccffffff)
    val focusedBorderWidth: Dp = 2.dp

    BoxWithConstraints(
        modifier =
            modifier
                .focusRequester(focusRequester)
                .focusable(interactionSource = interactionSource)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyDown) {
                        when (it.key) {
                            Key.DirectionRight -> {
                                val newValue = (value + step).coerceIn(valueRange)
                                onValueChange(newValue)
                                true
                            }
                            Key.DirectionLeft -> {
                                val newValue = (value - step).coerceIn(valueRange)
                                onValueChange(newValue)
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                },
    ) {
        val density = LocalDensity.current
        with(density) {
            maxWidth.toPx()
        }
        Box(
            modifier = Modifier.height(thumbRadius * 2),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerY = size.height / 2
                val percent = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
                val thumbX = percent * size.width

                if (useGradient) {
                    drawRoundRect(
                        brush =
                            Brush.linearGradient(
                                colors = listOf(trackGradientFrom, trackGradientTo),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, 0f),
                            ),
                        topLeft = Offset(0f, centerY - trackHeight.toPx() / 2),
                        size = Size(size.width, trackHeight.toPx()),
                        cornerRadius = CornerRadius(trackHeight.toPx() / 2),
                    )
                } else {
                    // Inactive track
                    drawRoundRect(
                        color = trackInactiveColor,
                        topLeft = Offset(0f, centerY - trackHeight.toPx() / 2),
                        size = Size(size.width, trackHeight.toPx()),
                        cornerRadius = CornerRadius(trackHeight.toPx() / 2),
                    )

                    // Active track
                    drawRoundRect(
                        color = trackColor,
                        topLeft = Offset(0f, centerY - trackHeight.toPx() / 2),
                        size = Size(thumbX, trackHeight.toPx()),
                        cornerRadius = CornerRadius(trackHeight.toPx() / 2),
                    )
                }

                if (isFocused) {
                    drawCircle(
                        color = focusedBorderColor,
                        center = Offset(thumbX, centerY),
                        radius = thumbRadius.toPx() + focusedBorderWidth.toPx(),
                        style = Stroke(width = focusedBorderWidth.toPx()),
                    )
                }

                // Thumb
                drawCircle(
                    color = thumbColor,
                    center = Offset(thumbX, centerY),
                    radius = thumbRadius.toPx(),
                )
            }
        }
    }
}
