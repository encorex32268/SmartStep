package com.lihan.smartstep.stepcount.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager

fun Context.isIgnoringBatteryOptimizations(): Boolean {
    val packageName = this.packageName
    val powerManager= this.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(packageName)
}

fun Context.canTrackingStepsInAppBackground(): Boolean {
    val canRunInBackground = isIgnoringBatteryOptimizations()
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
    val hasActivityRecognitionPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        } else {
            checkSelfPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")  == PackageManager.PERMISSION_GRANTED
        }

    return  canRunInBackground && hasNotificationPermission && hasActivityRecognitionPermission
}
