package com.lihan.smartstep.dashboard.presentation.report.mapper

import com.lihan.smartstep.core.domain.model.DailyStep
import com.lihan.smartstep.core.domain.util.DateTimeHelper.getDayOfWeek
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI

fun DailyStep.toDailyInfoUI(): DailyInfoUI {
    return DailyInfoUI(
        dayOfWeek = createAt.getDayOfWeek(),
        status = DailyInfoStatus.Done,
        steps = steps,
        spentTime = spentTime,
        stepGoal = stepsGoal,
        calories = calories,
        distance = distance
    )
}