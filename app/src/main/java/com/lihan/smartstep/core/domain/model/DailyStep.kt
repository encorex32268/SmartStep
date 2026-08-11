package com.lihan.smartstep.core.domain.model

data class DailyStep(
    val id: Int?=null,
    val createAt: Long,
    val steps: Int,
    val stepsGoal: Int,
    val spentTime: Long,
    val calories: Int,
    val distance: Double
)