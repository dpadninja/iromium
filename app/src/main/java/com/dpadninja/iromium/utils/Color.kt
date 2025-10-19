package com.dpadninja.iromium.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

fun Color.toHex(withAlpha: Boolean = false): String {
    val r = (this.red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    val a = (alpha * 255).toInt()

    return if (withAlpha) {
        "#%02X%02X%02X%02X".format(a, r, g, b) // ARGB
    } else {
        "#%02X%02X%02X".format(r, g, b)
    }
}

fun Color.Companion.fromHex(hexString: String): Color {
    val cleanedHex = hexString.removePrefix("#")
    val colorLong = cleanedHex.toLong(16)
    return Color(colorLong)
}

@Serializable
data class RGBCorrection(
    var red: Float = 1f,
    var green: Float = 1f,
    var blue: Float = 1f,
)

data class Hsv(
    val h: Float,
    val s: Float,
    val v: Float,
)

fun rgbCalibrate(
    color: Color,
    correction: RGBCorrection,
): Color {
    Log.d("RGB Calibrator", "input: $color, HEX: ${color.toHex()}")
    val outputColor =
        Color(
            (color.red * correction.red).coerceIn(0f, 1f),
            (color.green * correction.green).coerceIn(0f, 1f),
            (color.blue * correction.blue).coerceIn(0f, 1f),
        )
    Log.d("RGB Calibrator", "output: $outputColor, HEX: ${outputColor.toHex()}")
    return outputColor
}

fun rgbToHsv(color: Color): Hsv {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val d = max - min
    val h =
        when {
            d == 0f -> 0f
            max == r -> (60f * ((g - b) / d % 6f)).let { if (it < 0) it + 360f else it }
            max == g -> 60f * ((b - r) / d + 2f)
            else -> 60f * ((r - g) / d + 4f)
        }
    val s = if (max == 0f) 0f else d / max
    return Hsv((h + 360f) % 360f, s, max)
}

fun hsvToRgb(hsv: Hsv): Color {
    val h = (hsv.h % 360f + 360f) % 360f
    val s = hsv.s.coerceIn(0f, 1f)
    val v = hsv.v.coerceIn(0f, 1f)

    val c = v * s
    val x = c * (1 - kotlin.math.abs((h / 60f) % 2 - 1))
    val m = v - c

    val (rp, gp, bp) =
        when {
            h < 60f -> Triple(c, x, 0f)
            h < 120f -> Triple(x, c, 0f)
            h < 180f -> Triple(0f, c, x)
            h < 240f -> Triple(0f, x, c)
            h < 300f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
    return Color(rp + m, gp + m, bp + m)
}

fun genSimilarColors(
    color: Color,
    count: Int = 5,
    maxDeltaHue: Float = 60f,
    minValue: Float = 0.6f,
    minSaturation: Float = 0.2f,
    maxValue: Float = 1f,
): List<Color> {
    val baseHsv = rgbToHsv(color)
    val result = mutableListOf<Hsv>()

    val step = maxDeltaHue * 2 / count
    for (i in 0 until count) {
        val offset = -maxDeltaHue + i * step
        val h = (baseHsv.h + offset + 360f) % 360f
        var s = baseHsv.s
        var v = baseHsv.v

        if (v < minValue) v = minValue
        if (v > maxValue) v = maxValue
        if (s < minSaturation) s = minSaturation

        result.add(Hsv(h, s, v))
    }

    return listOf(color) + result.map { hsvToRgb(it) }
}
