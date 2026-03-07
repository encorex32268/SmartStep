package com.lihan.smartstep.stepcount.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface DailyStepDao {

    @Upsert
    fun upsert(dailyStepEntity: DailyStepEntity)

    @Query("SELECT * FROM dailystepentity WHERE timestamp=:timestamp")
    fun getDailyStep(timestamp: Long): Flow<DailyStepEntity>

}