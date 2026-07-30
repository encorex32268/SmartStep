package com.lihan.smartstep.core.domain

import com.lihan.smartstep.core.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow

interface DailyStepsRepository {

    suspend fun upsert(dailyStep: DailyStep)

    suspend fun updateStepsByDate(dateTime: Long, steps: Int)

    fun getWeekDailyStepsList(): Flow<List<DailyStep>>

}