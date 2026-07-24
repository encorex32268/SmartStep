package com.lihan.smartstep.dashboard.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Context.openSettings(){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivity(intent)
}