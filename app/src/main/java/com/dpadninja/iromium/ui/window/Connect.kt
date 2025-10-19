package com.dpadninja.iromium.ui.window
import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Power
import androidx.compose.material.icons.rounded.PowerOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dpadninja.iromium.AppContext
import com.dpadninja.iromium.R
import com.dpadninja.iromium.ble.BleManager
import com.dpadninja.iromium.ble.BleStatus
import com.dpadninja.iromium.ble.await
import com.dpadninja.iromium.ui.core.BasicWindow
import com.dpadninja.iromium.ui.core.Container
import com.dpadninja.iromium.ui.core.MacInput
import com.dpadninja.iromium.ui.core.Panel
import com.dpadninja.iromium.ui.core.UIButton
import com.dpadninja.iromium.ui.core.textColor
import com.dpadninja.iromium.ui.views.AppViewModel
import setColor
import togglePower

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun Connect(
    navController: NavHostController,
    view: AppViewModel,
) {
    val uiState by view.uiState.collectAsState()
    val testAddress by view.testAddress.collectAsState()
    var isAddressReady by remember { mutableStateOf(false) }
    val link = stringResource(R.string.setup_guide)

    Box(contentAlignment = Alignment.Center) {
        BasicWindow(modifier = Modifier.width(320.dp)) {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Connect Device",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MacInput(
                    modifier = Modifier.width(200.dp),
                    address = if (testAddress != "") testAddress else uiState.address,
                    onAddressFilled = {
                        view.setTestAddress(it)
                        isAddressReady = true
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                UIButton(
                    small = true,
                    text = "Setup guide",
                    onClick = {
                        view.qrCodeText = link
                        navController.navigate("qrcode")
                    },
                )
            }
        }
        if (isAddressReady) {
            Connection(
                address = testAddress,
                onConnected = {
                    navController.navigate("test")
                },
                onFailed = {
                    isAddressReady = false
                },
            )
        }
    }
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun Connection(
    address: String,
    onConnected: () -> Unit,
    onFailed: () -> Unit,
) {
    val context: Context = AppContext.context

    LaunchedEffect(Unit) {
        BleManager.disconnect()
        BleManager.connect(address)
        when (BleManager.status.await(BleStatus.READY)) {
            true -> {
                Toast.makeText(context, "Ready!", Toast.LENGTH_SHORT).show()
                onConnected()
            }
            false -> {
                Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show()
                onFailed()
            }
        }
    }
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
        )
    }
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun Test(
    navController: NavHostController,
    view: AppViewModel,
) {
    var isOn by remember { mutableStateOf(true) }
    if (isOn) { togglePower(true) } else { togglePower(false)}
    BasicWindow(modifier = Modifier.width(320.dp)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Test your LED",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Connected to ${view.testAddress.collectAsState().value}!", color = textColor, fontWeight = FontWeight.Bold)
        Text(
            "Test the basic colors and the ability to turn on and turn off the LED. If everything is working the device is supported and you can proceed.",
            color = textColor,
        )
        val testPresets = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan)
        Container {
            Panel(
                initialIndex = -1,
                presets = testPresets,
                onColorClick = { setColor(testPresets[it]) },
            )
        }
        Switch(
            modifier = Modifier.padding(5.dp),
            checked = isOn,
            onCheckedChange = { isOn = it },
            thumbContent = {
                if (isOn) {
                    Icon(Icons.Rounded.Power, contentDescription = "On")
                } else {
                    Icon(Icons.Rounded.PowerOff, contentDescription = "Off")
                }
            },
        )
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UIButton(
                small = true,
                text = "Finish setup",
                onClick = {
                    isOn = true
                    view.setAddress(view.testAddress.value)
                    navController.navigate("main") { popUpTo(0) }
                },
            )
        }
    }
}
