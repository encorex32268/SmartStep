package com.lihan.smartstep.core.data

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.core.net.toUri


fun Context.openPowerManagerIntent(){
    try {
        val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:$packageName".toUri()
        }
        this.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        this.startActivity(intent)
    }
}