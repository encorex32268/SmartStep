@file:OptIn(ExperimentalTime::class)

package com.lihan.smartstep.core.data.datastore.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

@Dao
interface DailyStepDao {

    @Upsert
    suspend fun upsert(dailyStepEntity: DailyStepEntity)

    @Query("SELECT * FROM dailystepentity WHERE dayTimestamp <=:endTime AND dayTimestamp >=:startTime")
    fun getDailyStep(
        startTime: Long,
        endTime: Long
    ): Flow<List<DailyStepEntity>>

    @Query("SELECT * FROM dailystepentity WHERE dayTimestamp=:timestamp")
    fun getDailyStepByDateTimestamp(
        timestamp: Long
    ): Flow<DailyStepEntity?>


}
