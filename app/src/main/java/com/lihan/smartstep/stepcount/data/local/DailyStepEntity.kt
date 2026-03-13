package com.lihan.smartstep.stepcount.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * goal: 6,000
 * steps: 2,345
 * time: 60_000 -> 1 min
 * dayTimestamp: 12345678 -> 2026/03/06 00:00:00
 */
@Entity
data class DailyStepEntity(
    @PrimaryKey
    val dayTimestamp: Long,
    val goal: Long,
    val steps: Long,
    val time: Long
)
