
package com.dpadninja.iromium
import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.dpadninja.iromium.ble.BleManager
import com.dpadninja.iromium.ble.BleStatus
import com.dpadninja.iromium.ble.await
import com.dpadninja.iromium.ui.views.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import setColor
import togglePower

class LedControlService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val receiver =
        object : BroadcastReceiver() {
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                if (!AppSettings.syncWithTVState) { return }
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> {
                        togglePower(true)
                    }
                    Intent.ACTION_SCREEN_OFF, Intent.ACTION_SHUTDOWN -> {
                        togglePower(false)
                    }
                }
            }
        }
    companion object {
        var isRunning = false
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun autostartActions() {
        BleManager.connect()
        if (BleManager.status.await(BleStatus.READY)) {
            if (! AppSettings.syncWithTVState) { return }
            togglePower(true)
            delay(1000)
            setColor(AppSettings.preset.color)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        setupNotification()
        Toast
            .makeText(
                AppContext.context,
                "Iromium service starting...",
                Toast.LENGTH_SHORT,
            ).show()
            isRunning = true
        val filter =
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SHUTDOWN)
            }
        registerReceiver(receiver, filter)
        serviceScope.launch  {
            autostartActions()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        isRunning = false
    }

    private fun setupNotification() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("iromium", "iromium", NotificationManager.IMPORTANCE_LOW)
        nm.createNotificationChannel(channel)
        val notification =
            Notification
                .Builder(this, "iromium")
                .setContentTitle("Iromium service")
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
