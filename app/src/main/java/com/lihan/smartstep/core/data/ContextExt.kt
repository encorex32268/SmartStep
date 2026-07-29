package com.lihan.smartstep.core.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

val Context.hasActivityRecognitionPermission
    get() = this.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED