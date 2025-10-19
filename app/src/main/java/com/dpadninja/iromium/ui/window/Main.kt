package com.dpadninja.iromium.ui.window

import android.Manifest
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dpadninja.iromium.AppContext
import com.dpadninja.iromium.ui.core.BasicWindow
import com.dpadninja.iromium.ui.core.Container
import com.dpadninja.iromium.ui.core.IconUIButton
import com.dpadninja.iromium.ui.core.Panel
import com.dpadninja.iromium.ui.core.buttonBackgroundColorFocused
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel
import setColor

@OptIn(ExperimentalStdlibApi::class)
@Composable
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun Main(
    navController: NavController,
    view: AppViewModel,
) {
    val context = LocalContext.current
    val uiState by view.uiState.collectAsState()
    BackHandler {
        val activity = context as? Activity
        activity?.finish()
    }
    setColor(uiState.preset.color)

    BasicWindow(Modifier.width(280.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Make It Yours",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
                IconUIButton(
                    small = true,
                    onClick = {
                        navController.navigate("settings")
                    },
                    icon = Icons.Rounded.Settings,
                    text = "Settings",
                    color = Color.Transparent,
                    focusedColor = buttonBackgroundColorFocused,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column {
            Container(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                Panel(
                    initialIndex = uiState.presetIdx,
                    saveFocus = true,
                    presets = uiState.presets.map { it.labelColor },
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                    onColorClick = {
                        view.setPresetIdx(it)
                    },
                    onColorLongPress = { navController.navigate("editPreset") },
                )
            }
        }
    }
}
