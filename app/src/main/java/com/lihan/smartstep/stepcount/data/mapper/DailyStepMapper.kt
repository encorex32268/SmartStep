package com.lihan.smartstep.stepcount.data.mapper

import com.lihan.smartstep.stepcount.data.local.DailyStepEntity
import com.lihan.smartstep.stepcount.domain.model.DailyStep

fun DailyStepEntity.toDomain(): DailyStep {
    return DailyStep(
        goal = goal,
        steps = steps,
        timestamp = timestamp
    )
}

fun DailyStep.toEntity(): DailyStepEntity {
    return DailyStepEntity(
        goal = goal,
        steps = steps,
        timestamp = timestamp
    )
}