package com.lihan.smartstep.stepcount.domain.repository

import com.lihan.smartstep.stepcount.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow

interface SmartStepRepository {

    fun updateDailyStep(dailyStep: DailyStep)

    fun getDailyStep(timestamp: Long): Flow<DailyStep>
}