package com.dpadninja.iromium

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.dpadninja.iromium.ui.views.AppSettings
import java.lang.ref.WeakReference


object AppContext {
    private var _context: WeakReference<Context>? = null
    fun init(app: Application) {
        _context = WeakReference(app.applicationContext)
    }
    val context: Context
        get() = _context?.get()
            ?: error("AppContext not initialized")
}

class App : Application() {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate() {
        super.onCreate()
        AppContext.init(this)
        AppSettings.init(this)
        ContextCompat.startForegroundService(
            AppContext.context,
            Intent(this, LedControlService::class.java),
        )
        }
}

