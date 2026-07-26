package com.lihan.smartstep.core.presentation.util

val Int.withZeroDisplay: String
    get() = "%02d".format(this)