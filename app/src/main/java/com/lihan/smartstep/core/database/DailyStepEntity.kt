package com.lihan.smartstep.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lihan.smartstep.core.domain.model.DailyStep

@Entity
data class DailyStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val createAt: Long,
    val steps: Int,
    val stepGoal: Int,
    val spentTime: Long
)

fun DailyStepEntity.toDomain(): DailyStep {
    return DailyStep(
        id = id,
        createAt = createAt,
        steps = steps,
        stepsGoal = stepGoal,
        spentTime = spentTime
    )
}