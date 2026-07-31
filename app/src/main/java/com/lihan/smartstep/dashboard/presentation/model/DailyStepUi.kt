package com.lihan.smartstep.dashboard.presentation.model

import com.lihan.smartstep.core.domain.model.DailyStep
import java.text.NumberFormat

data class DailyStepUi(
    val day: String,
    val steps: Int,
    val stepsGoal: Int
) {
    val stepGoalProgress: Float
        get() {
            if (stepsGoal <= 0) return 0f
            return (steps / stepsGoal.toFloat()).coerceIn(0f, 1f)
        }

    val displaySteps: String
        get() = NumberFormat.getNumberInstance().format(steps)

}

fun DailyStep.toUi(): DailyStepUi {
    return DailyStepUi(
        steps = steps,
        stepsGoal = stepsGoal,
        day = ""
    )
}