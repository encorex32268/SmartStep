package com.lihan.smartstep.dashboard.presentation.report

import com.lihan.smartstep.core.domain.util.DateTimeHelper
import com.lihan.smartstep.core.presentation.util.toNumberString
import com.lihan.smartstep.core.presentation.util.toPureInt
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI
import java.time.DayOfWeek
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class ReportState(
    val isLoading: Boolean = false,
    val reportType: ReportType = ReportType.Steps,
    val dailyInfo: List<DailyInfoUI> = emptyList(),

    val week: Int = 0,
    val isNextWeekEnabled: Boolean = false,
    val isPreviousWeekEnabled: Boolean = false,
    val currentValue: String = ""
){
    val currentWeek: String
        get() = DateTimeHelper.getWeekDisplay(week.toLong())

    val average: String
        get(){
            return when(reportType){
                ReportType.Steps -> (dailyInfo.sumOf { it.steps.toPureInt() } / 7) .toNumberString()
                ReportType.Calories -> (dailyInfo.sumOf { it.calories.toPureInt() } / 7) .toNumberString()
                ReportType.Minutes -> (dailyInfo.sumOf { it.spentTime.toLong() } /7).toDuration(DurationUnit.MINUTES) .toString()
                ReportType.Kilometers -> (dailyInfo.sumOf { it.distance.toDouble() } / 7) .toString()
            }
        }

}