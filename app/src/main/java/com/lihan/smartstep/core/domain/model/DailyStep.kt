package com.lihan.smartstep.core.domain.model

import com.lihan.smartstep.core.database.DailyStepEntity

data class DailyStep(
    val id: Int?=null,
    val createAt: Long,
    val steps: Int,
    val stepsGoal: Int,
    val spentTime: Long
)

fun DailyStep.toDailyStepsEntity(): DailyStepEntity {
    return DailyStepEntity(
        id = id,
        createAt = createAt,
        steps = steps,
        stepGoal = stepsGoal,
        spentTime = spentTime
    )
}