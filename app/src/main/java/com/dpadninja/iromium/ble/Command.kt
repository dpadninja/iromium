import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.Color
import com.dpadninja.iromium.ble.BleManager
import com.dpadninja.iromium.ble.brightnessPacket
import com.dpadninja.iromium.ble.colorPacket
import com.dpadninja.iromium.ble.powerOffPacket
import com.dpadninja.iromium.ble.powerOnPacket
import com.dpadninja.iromium.ui.views.AppSettings
import com.dpadninja.iromium.utils.rgbCalibrate
import com.dpadninja.iromium.utils.toHex

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun togglePower(
    value: Boolean,
) {
    val powerCmd = if (value) powerOnPacket() else powerOffPacket()
    BleManager.send(powerCmd)
    if (value) BleManager.send(brightnessPacket(100))
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun setColor(color: Color) {
    val calibratedColor = rgbCalibrate(color, AppSettings.rgbCorrection)
    BleManager.send(colorPacket(calibratedColor.toHex()))
}

