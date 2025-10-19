package com.dpadninja.iromium.helpers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

open class PreferenceUtil {
    private lateinit var prefs: SharedPreferences

    fun init(app: Application) {
        prefs = app.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    @Suppress("unused")
    fun keyExists(key: String): Boolean = prefs.contains(key)

    fun putString(
        key: String,
        value: String,
    ) {
        prefs.edit { putString(key, value) }
    }

    fun putInt(
        key: String,
        value: Int,
    ) {
        prefs.edit { putInt(key, value) }
    }

    fun putBoolean(
        key: String,
        value: Boolean,
    ) {
        prefs.edit { putBoolean(key, value) }
    }

    fun getString(
        key: String,
        defValue: String,
    ): String = prefs.getString(key, defValue) ?: ""

    fun getInt(
        key: String,
        defValue: Int,
    ): Int = prefs.getInt(key, defValue)

    fun getBoolean(
        key: String,
        defValue: Boolean,
    ): Boolean = prefs.getBoolean(key, defValue)
}
