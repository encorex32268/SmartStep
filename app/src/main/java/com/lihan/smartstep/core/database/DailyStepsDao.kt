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
        WHERE createAt >= (strftime('%s', datetime('now', 'localtime', 'start of day', 'weekday 0', '-6 days')) * 1000)                                                                                                              
          AND createAt <  (strftime('%s', datetime('now', 'localtime', 'start of day', 'weekday 0', '+1 day')) * 1000)                                                                                                               
    """)
    fun getWeekDailyStepsList(): Flow<List<DailyStepEntity>>



}