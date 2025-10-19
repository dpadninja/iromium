package com.dpadninja.iromium

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat


fun hasBTPermission(): Boolean {
    if (Build.VERSION.SDK_INT > 30) {
    return ContextCompat.checkSelfPermission(
        AppContext.context,
        Manifest.permission.BLUETOOTH_CONNECT,
    ) == PackageManager.PERMISSION_GRANTED
    }
    return true
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestBluetoothPermission(onGranted: () -> Unit) {
    var hasPermission by remember { mutableStateOf(hasBTPermission()) }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasPermission = granted
                if (granted) onGranted()
            },
        )

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            onGranted()
        }
    }
}
