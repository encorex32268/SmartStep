package com.lihan.smartstep.core.data

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

fun Context.isNotInPowerManagerWhiteList(): Boolean {
    val packageName = this.packageName
    val powerManager= this.getSystemService(Context.POWER_SERVICE) as PowerManager
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}


@Composable
fun isNotInPowerManagerWhiteList(): Boolean {
    val context = LocalContext.current
    val packageName = context.packageName
    val powerManager= context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}

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