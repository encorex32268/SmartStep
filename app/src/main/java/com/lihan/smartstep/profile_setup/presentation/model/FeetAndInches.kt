package com.lihan.smartstep.profile_setup.presentation.model

import kotlin.math.roundToInt

data class FeetAndInches(
    val feet: Int,
    val inches: Int
) {
    override fun toString(): String = "$feet'$inches\""
}

fun String.feetInchesToCm(): String? {
    val regex = """^(\d+)'\s*(\d+)"?"$""".toRegex()
    val matchResult = regex.find(this.trim()) ?: return null
    val (feetStr, inchesStr) = matchResult.destructured
    val feet = feetStr.toInt()
    val inches = inchesStr.toInt()
    return (((feet * 12) + inches) * 2.54).roundToInt().toString()
}

fun Double.toFeetAndInches(): FeetAndInches {
    val totalInches = (this / 2.54).roundToInt()
    val feet = totalInches / 12
    val inches = totalInches % 12
    return FeetAndInches(feet, inches)
}
fun Int.toFeetAndInches(): FeetAndInches = this.toDouble().toFeetAndInches()



