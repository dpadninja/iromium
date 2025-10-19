package com.dpadninja.iromium
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.dpadninja.iromium.ble.BleManager
import com.dpadninja.iromium.ui.ComposeApp

class Activity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            ) {
                finish()
            }
        } else {
            // Android 11–12
            onBackPressedDispatcher.addCallback(this) {
                finish()
            }
        }
        setContent {
            RequestBluetoothPermission(onGranted = { BleManager.btPermissionGranted = true })
            ComposeApp()
        }
    }
}
