package com.lihan.smartstep.stepcount.presentation.mapper

import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.presentation.epochMilToDayOfWeekShort
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI

fun DailyStep.toUi(): DailyStepUI {
    return DailyStepUI(
        steps = steps.toString(),
        goalSteps = goal.toString(),
        date = timestamp.epochMilToDayOfWeekShort(),
        timestamp = timestamp
    )
}