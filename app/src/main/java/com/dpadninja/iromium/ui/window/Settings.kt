package com.dpadninja.iromium.ui.window

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dpadninja.iromium.ui.core.SettingItem
import com.dpadninja.iromium.ui.core.SettingSwitchItem
import com.dpadninja.iromium.ui.core.BasicWindow
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel

@Composable
fun Settings(
    navController: NavController,
    view: AppViewModel,
) {
    val uiState by view.uiState.collectAsState()

    BasicWindow {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Settings",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        SettingSwitchItem(
            caption = "Sync with TV",
            description = "LED turns off when the TV is off and comes back on with the TV",
            switchState = uiState.syncWithTVState,
            onClick = { view.setSyncWithTVState(it) },
        )
        SettingItem(
            caption = "Calibrate color channels",
            description = "Adjust red, green, and blue channel balance for more natural colors",
            onClick = { navController.navigate("calibrator") },
        )
        SettingItem(
            caption = "Re-Pair Device",
            description = "Restart the pairing process with the selected device",
            onClick = { navController.navigate("connect") },
        )
    }
}
