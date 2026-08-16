package com.lihan.smartstep.dashboard.presentation.report.model

import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus

data class DailyInfoUI(
    val dayOfWeek: String,
    val status: DailyInfoStatus,
    val spentTime: String,
    val stepGoal: String,
    val calories: String,
    val distance: String,
    val steps: String
)