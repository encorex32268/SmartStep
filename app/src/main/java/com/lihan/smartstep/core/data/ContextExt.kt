package com.lihan.smartstep.core.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

val Context.hasActivityRecognitionPermission
    get() = this.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED

val Context.hasNotificationPermission: Boolean
    get() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }