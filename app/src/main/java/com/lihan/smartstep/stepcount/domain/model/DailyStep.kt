package com.lihan.smartstep.stepcount.domain.model

data class DailyStep(
    val goal: Long,
    val steps: Long,
    val timestamp: Long
)