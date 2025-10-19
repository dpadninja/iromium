package com.dpadninja.iromium.ui.window

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dpadninja.iromium.ui.core.BasicWindow
import com.dpadninja.iromium.ui.core.UIButton
import com.dpadninja.iromium.ui.core.UISliderHorizontal
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel
import setColor

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun UICalibratorSlider(
    color: Color,
    correction: Float,
    onValueChange: (Float) -> Unit,
) {
    var corr by remember { mutableFloatStateOf(correction) }
    UISliderHorizontal(
        useGradient = true,
        trackGradientFrom = color.copy(alpha = 0.1f),
        trackGradientTo = color.copy(alpha = 0.8f),
        trackInactiveColor = color,
        trackColor = color,
        value = corr / 2,
        step = 0.1f,
        onValueChange = {
            corr = it * 2
            onValueChange(corr)
        },
        thumbColor = color,
    )
}

@Composable
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun Calibrator(
    navController: NavController,
    view: AppViewModel,
) {
    val uiState by view.uiState.collectAsState()
    var rgbCorrection by remember { mutableStateOf(uiState.rgbCorrection) }
    setColor(uiState.preset.color)
    BasicWindow(Modifier.width(280.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Calibrate color channels",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().align(Alignment.CenterHorizontally),
        ) {
            UICalibratorSlider(
                color = Color(0xFFFF4D4D),
                correction = rgbCorrection.red,
                onValueChange = { rgbCorrection = rgbCorrection.copy(red = it) },
            )
            UICalibratorSlider(
                color = Color(0xFF5DFF4D),
                correction = rgbCorrection.green,
                onValueChange = { rgbCorrection = rgbCorrection.copy(green = it) },
            )
            UICalibratorSlider(
                color = Color(0xFF4D4EFF),
                correction = rgbCorrection.blue,
                onValueChange = { rgbCorrection = rgbCorrection.copy(blue = it) },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        UIButton(small = true, text = "Save", onClick = {
            view.setRGBCorrection(rgbCorrection)
            navController.popBackStack()
        })
    }
}
