package com.lihan.smartstep.stepcount.data.repository

import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.mapper.toDomain
import com.lihan.smartstep.stepcount.data.mapper.toEntity
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SmartStepRepositoryImpl(
    private val database: SmartStepDatabase,
    private val userInfoDataStore: UserInfoDataStore
): SmartStepRepository{

    override fun updateDailyStep(dailyStep: DailyStep) {
        database.dailyStepDao.upsert(dailyStep.toEntity())
    }

    override fun getDailyStep(timestamp: Long): Flow<DailyStep> {
        return database.dailyStepDao.getDailyStep(timestamp).map { it.toDomain() }
    }


}