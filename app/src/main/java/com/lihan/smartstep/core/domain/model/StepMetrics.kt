package com.lihan.smartstep.core.domain.model

import kotlin.time.Duration


data class StepMetrics(
    val time: Duration = Duration.ZERO,
    val steps: Int = 0,
    val kcal: Int = 0,
    val distance: Double = 0.0,
    val stepGoal: Int = 10000,
    val progress: Float = 0f
)

fun Double.formattedString() = "%.1f".format(this)
