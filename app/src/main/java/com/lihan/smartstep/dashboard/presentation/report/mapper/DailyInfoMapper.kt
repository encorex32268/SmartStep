package com.lihan.smartstep.dashboard.presentation.report.mapper

import com.lihan.smartstep.core.domain.model.DailyStep
import com.lihan.smartstep.core.domain.util.DateTimeHelper.getDayOfWeek
import com.lihan.smartstep.core.presentation.util.toNumberString
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun DailyStep.toDailyInfoUI(): DailyInfoUI {
    return DailyInfoUI(
        dayOfWeek = createAt.getDayOfWeek(),
        status = DailyInfoStatus.Done,
        steps = steps.toNumberString(),
        spentTime = spentTime.toString(),
        stepGoal = stepsGoal.toNumberString(),
        calories = calories.toNumberString(),
        distance = distance.toString()
    )
}