package com.lihan.smartstep.stepcount.domain.model

data class StepData(
    val goalSteps: Long = 0,
    val steps: Long = 0,
    val countingTimestamp: Long = 0,
    val calories: Long = 0,
    val distance: Double = 0.0
)