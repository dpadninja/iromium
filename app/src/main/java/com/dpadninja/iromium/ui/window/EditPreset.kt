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
import androidx.compose.runtime.mutableIntStateOf
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
import com.dpadninja.iromium.ui.core.Container
import com.dpadninja.iromium.ui.core.Panel
import com.dpadninja.iromium.ui.core.UIButton
import com.dpadninja.iromium.ui.core.UISliderHorizontal
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel
import com.dpadninja.iromium.utils.genSimilarColors
import setColor

@OptIn(ExperimentalStdlibApi::class)
@Composable
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun EditPreset(
    navController: NavController,
    view: AppViewModel,
) {
    val uiState by view.uiState.collectAsState()

    var preset by remember { mutableStateOf(uiState.presets[uiState.presetIdx]) }
    var color by remember { mutableStateOf(preset.color) }
    var labelIndex by remember { mutableIntStateOf(preset.labelIndex) }
    val labelColors = genSimilarColors(color)
    setColor(color)

    BasicWindow(Modifier.width(280.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Edit preset",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            val red = Color(0xFFFF4D4D)
            val green = Color(0xFF5DFF4D)
            val blue = Color(0xFF4D4EFF)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().align(Alignment.CenterHorizontally),
            ) {
                UISliderHorizontal(
                    trackColor = red.copy(alpha = 0.7f),
                    trackInactiveColor = red.copy(alpha = 0.3f),
                    value = color.red,
                    step = 3f / 255f,
                    onValueChange = {
                        color = color.copy(red = it)
                    },
                    thumbColor = red,
                )
                UISliderHorizontal(
                    trackColor = green.copy(alpha = 0.7f),
                    trackInactiveColor = green.copy(alpha = 0.3f),
                    value = color.green,
                    step = 3f / 255f,
                    onValueChange = {
                        color = color.copy(green = it)
                    },
                    thumbColor = green,
                )
                UISliderHorizontal(
                    trackColor = blue.copy(alpha = 0.7f),
                    trackInactiveColor = blue.copy(alpha = 0.3f),
                    value = color.blue,
                    step = 3f / 255f,
                    onValueChange = {
                        color = color.copy(blue = it)
                    },
                    thumbColor = blue,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Container(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Panel(
                initialIndex = labelIndex,
                presets = labelColors,
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                onColorClick = { labelIndex = it },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        UIButton(small = true, text = "Save", onClick = {
            preset.color = color
            preset.labelIndex = labelIndex
            preset.labelColor = labelColors[labelIndex]
            view.setPreset(uiState.presetIdx, preset)
            navController.popBackStack()
        })
    }
}
