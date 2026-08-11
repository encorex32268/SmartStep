package com.lihan.smartstep.core.data.mapper

import com.lihan.smartstep.core.database.DailyStepEntity
import com.lihan.smartstep.core.domain.model.DailyStep

fun DailyStep.toEntity(): DailyStepEntity {
    return DailyStepEntity(
        createAt = createAt,
        steps = steps,
        stepGoal = stepsGoal,
        spentTime = spentTime,
        calories = calories,
        distance = distance
    )
}

fun DailyStepEntity.toDomain(): DailyStep {
    return DailyStep(
        id = id,
        createAt = createAt,
        steps = steps,
        stepsGoal = stepGoal,
        spentTime = spentTime,
        calories = calories,
        distance = distance
    )
}