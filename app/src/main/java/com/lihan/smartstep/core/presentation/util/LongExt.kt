package com.lihan.smartstep.core.presentation.util

import java.text.NumberFormat

private val numberFormat = NumberFormat.getNumberInstance()

fun Long.toNumberString(): String {
    return numberFormat.format(this)
}