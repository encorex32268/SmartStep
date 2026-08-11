package com.lihan.smartstep.dashboard.presentation.report.model

import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus

data class DailyInfoUI(
    val dayOfWeek: String,
    val status: DailyInfoStatus,
    val spentTime: Long,
    val stepGoal: Int,
    val calories: Int,
    val distance: Double,
    val steps: Int
)