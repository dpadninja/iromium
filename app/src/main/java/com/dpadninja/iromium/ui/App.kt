package com.dpadninja.iromium.ui

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dpadninja.iromium.ui.views.AppSettings
import com.dpadninja.iromium.ui.views.AppViewModel
import com.dpadninja.iromium.ui.window.Calibrator
import com.dpadninja.iromium.ui.window.Connect
import com.dpadninja.iromium.ui.window.EditPreset
import com.dpadninja.iromium.ui.window.Main
import com.dpadninja.iromium.ui.window.QRWindow
import com.dpadninja.iromium.ui.window.Settings
import com.dpadninja.iromium.ui.window.Test

@OptIn(ExperimentalAnimationApi::class)
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    val view: AppViewModel = viewModel()
    val window = if (AppSettings.address != "") "main" else "connect"
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = window,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable("connect") { Connect(navController, view) }
            composable("test") { Test(navController, view) }
            composable("settings") { Settings(navController, view) }
            composable("main") { Main(navController, view) }
            composable("editPreset") { EditPreset(navController, view) }
            composable("calibrator") { Calibrator(navController, view) }
            composable("qrcode") { QRWindow( view) }
        }
    }
}
