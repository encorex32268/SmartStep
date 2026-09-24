package com.lihan.smartstep.core.data

import com.lihan.smartstep.core.data.mapper.toDomain
import com.lihan.smartstep.core.data.mapper.toEntity
import com.lihan.smartstep.core.database.DailyStepsDao
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDailyStepsRepository(
    private val dailyStepsDao: DailyStepsDao
): DailyStepsRepository{

    override suspend fun upsert(dailyStep: DailyStep) {
        dailyStepsDao.upsert(dailyStep.toEntity())
    }

    override suspend fun updateStepsByDate(dateTime: Long, steps: Int) {
        dailyStepsDao.updateStepsByDate(dateTime, steps)
    }

    override fun getWeekDailyStepsList(startTimestamp: Long , endTimestamp: Long): Flow<List<DailyStep>> {
        return dailyStepsDao
            .getWeekDailyStepsList(
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp
            )
            .map { dailyStepsEntities ->
                dailyStepsEntities.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun getDailyStepByDate(dateTime: Long): DailyStep? {
        return dailyStepsDao.getDailyStepByDate(dateTime)?.toDomain()
    }
}