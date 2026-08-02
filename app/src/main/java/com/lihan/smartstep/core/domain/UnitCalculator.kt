package com.lihan.smartstep.core.domain

import com.lihan.smartstep.core.data.model.Gender
import kotlin.math.roundToInt

object UnitCalculator {

    fun calculateKcal(
        steps: Int,
        weightKg: Int,
        genderName: String,
    ): Int {
        val genderFactor = if (genderName == Gender.Male.name) 1.0 else 0.9
        val totalKcal = steps * weightKg * 0.005 * genderFactor
        return totalKcal.roundToInt()
    }

    fun calculateDistance(
        heightCm: Int,
        steps: Int,
    ): Double {
        val distance = (steps * (heightCm / 100f)) / 1000

        return distance.toDouble()
    }


}