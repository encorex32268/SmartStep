package com.lihan.smartstep.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepsDao {

    @Upsert
    suspend fun upsert(dailyStepEntity: DailyStepEntity)

    @Query("""
        UPDATE dailystepentity 
        SET steps = :steps 
        WHERE createAt = :dateTime
    """)
    suspend fun updateStepsByDate(dateTime: Long, steps: Int)

    @Query("""
        SELECT * FROM dailystepentity
        WHERE createAt >= datetime('now', 'localtime', 'weekday 0', '-6 days', 'start of day')
        AND createAt <  datetime('now', 'localtime', 'weekday 0', '+1 day', 'start of day');
    """)
    fun getWeekDailyStepsList(): Flow<List<DailyStepEntity>>



}