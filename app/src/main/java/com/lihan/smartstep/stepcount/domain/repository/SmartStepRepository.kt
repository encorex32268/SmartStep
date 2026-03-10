package com.lihan.smartstep.stepcount.domain.repository

import com.lihan.smartstep.stepcount.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow

interface SmartStepRepository {

    suspend fun updateDailyStep(dailyStep: DailyStep)

    fun getWeekDailySteps(timestamp: Long): Flow<List<DailyStep>>
}