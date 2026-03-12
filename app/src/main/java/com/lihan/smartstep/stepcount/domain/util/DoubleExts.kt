package com.lihan.smartstep.stepcount.domain.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Double.formatToString(): String {
    return String.format("%.1f", this)
}