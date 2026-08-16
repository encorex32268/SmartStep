package com.lihan.smartstep.core.presentation.util


fun Int.toNumberString(): String = "%,d".format(this)

fun String.toPureInt(): Int = filter { it.isDigit() }.toIntOrNull() ?: 0