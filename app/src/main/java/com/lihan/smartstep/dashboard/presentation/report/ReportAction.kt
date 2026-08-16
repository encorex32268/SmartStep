package com.lihan.smartstep.dashboard.presentation.report

import com.lihan.smartstep.dashboard.presentation.report.components.ReportType
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI

sealed interface ReportAction {
    data object OnBackClick: ReportAction
    data class OnReportTypeClick(val type: ReportType): ReportAction
    data class OnDailyInfoItemClick(val dailyInfoUI: DailyInfoUI): ReportAction
    data object OnNextWeekClick: ReportAction
    data object OnPreviousClick: ReportAction
}