package com.lihan.smartstep.core.presentation.util

import java.text.NumberFormat

val Int.withZeroDisplay: String
    get() = "%02d".format(this)


private val numberFormat = NumberFormat.getNumberInstance()

fun Int.toNumberString(): String {
    return numberFormat.format(this)
}