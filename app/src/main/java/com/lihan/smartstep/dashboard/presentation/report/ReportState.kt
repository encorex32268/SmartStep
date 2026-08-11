package com.lihan.smartstep.dashboard.presentation.report

import com.lihan.smartstep.core.domain.util.DateTimeHelper
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI
import java.time.DayOfWeek
import kotlin.random.Random

data class ReportState(
    val isLoading: Boolean = false,
    val reportType: ReportType = ReportType.Steps,
    val dailyInfo: List<DailyInfoUI> = DayOfWeek.entries.map {
        DailyInfoUI(
            dayOfWeek = it.name,
            steps = Random.nextInt(99,1000),
            status = DailyInfoStatus.entries.random(),
            calories = Random.nextInt(99,1000),
            distance = Random.nextInt(0,100).toDouble(),
            stepGoal = Random.nextInt(1000,10000),
            spentTime = Random.nextLong(10,900)
        )
    },

    val week: Int = 0,
    val currentValue: String = "",
    val average: String = ""
){
    val currentWeek: String
        get() = DateTimeHelper.getWeekDisplay(week.toLong())


}