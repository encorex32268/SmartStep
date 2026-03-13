@file:OptIn(ExperimentalTime::class)

package com.lihan.smartstep.stepcount.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Dao
interface DailyStepDao {

    @Upsert
    suspend fun upsert(dailyStepEntity: DailyStepEntity)

    @Query("SELECT * FROM dailystepentity WHERE dayTimestamp <=:endTime AND dayTimestamp >=:startTime")
    fun getDailyStep(
        startTime: Long,
        endTime: Long
    ): Flow<List<DailyStepEntity>>

}
