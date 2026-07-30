package com.lihan.smartstep.core.data

import com.lihan.smartstep.core.database.DailyStepsDao
import com.lihan.smartstep.core.database.toDomain
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.model.DailyStep
import com.lihan.smartstep.core.domain.model.toDailyStepsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDailyStepsRepository(
    private val dailyStepsDao: DailyStepsDao
): DailyStepsRepository{

    override suspend fun upsert(dailyStep: DailyStep) {
        dailyStepsDao.upsert(dailyStep.toDailyStepsEntity())
    }

    override suspend fun updateStepsByDate(dateTime: Long, steps: Int) {
        dailyStepsDao.updateStepsByDate(dateTime, steps)
    }

    override fun getWeekDailyStepsList(): Flow<List<DailyStep>> {
        return dailyStepsDao
            .getWeekDailyStepsList()
            .map { dailyStepsEntities ->
                dailyStepsEntities.map {
                    it.toDomain()
                }
            }
    }
}