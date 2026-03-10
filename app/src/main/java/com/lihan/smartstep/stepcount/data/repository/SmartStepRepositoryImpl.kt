package com.lihan.smartstep.stepcount.data.repository

import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.mapper.toDomain
import com.lihan.smartstep.stepcount.data.mapper.toEntity
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.ZoneId

class SmartStepRepositoryImpl(
    private val database: SmartStepDatabase
): SmartStepRepository{

    override suspend fun updateDailyStep(dailyStep: DailyStep) {
        database.dailyStepDao.upsert(dailyStep.toEntity())
    }

    override fun getWeekDailySteps(timestamp: Long): Flow<List<DailyStep>> {
        val zoneId = ZoneId.systemDefault()
        val targetZonedDateTime = java.time.Instant.ofEpochMilli(timestamp).atZone(zoneId)

        val dayOfWeek = targetZonedDateTime.dayOfWeek.value

        val daysBefore = if (dayOfWeek == 7) 0 else dayOfWeek
        val daysAfter = 6 - daysBefore

        val startTime = targetZonedDateTime.minusDays(daysBefore.toLong())
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
            .toInstant().toEpochMilli()

        val endTime = targetZonedDateTime.plusDays(daysAfter.toLong())
            .withHour(23).withMinute(59).withSecond(59).withNano(999)
            .toInstant().toEpochMilli()


        return database.dailyStepDao.getDailyStep(startTime = startTime, endTime = endTime)
            .map { dailyStepEntities ->
                dailyStepEntities.map {
                    it.toDomain()
                }
            }
    }

}