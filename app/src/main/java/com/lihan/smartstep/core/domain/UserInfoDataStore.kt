package com.lihan.smartstep.core.domain

import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow

interface UserInfoDataStore {

    suspend fun updateGender(gender: Gender)
    fun getGender(): Flow<Gender>

    suspend fun updateHeight(height: Int)
    fun getHeight(): Flow<Int>

    suspend fun updateHeightUnit(unitType: UnitType)
    fun getHeightUnit(): Flow<UnitType>

    suspend fun updateWeight(weight: Int)
    fun getWeight(): Flow<Int>

    suspend fun updateWeightUnit(unitType: UnitType)
    fun getWeightUnit(): Flow<UnitType>

    suspend fun updateIsSetting(isSet: Boolean)
    fun getIsSetting(): Flow<Boolean>

    suspend fun updateTotalStep(value: Long)
    fun getTotalStep(): Flow<Long>

    suspend fun updateIsShownBackgroundAccess(isShown: Boolean)
    fun getShownBackgroundAccess(): Flow<Boolean>

    suspend fun updateDeviceInitSteps(value: Long)
    fun getDeviceInitSteps(): Flow<Long>

    suspend fun updateTodaySteps(value: Long)
    fun getTodaySteps(): Flow<Long>

    suspend fun updateTempDailySteps(dailySteps: List<DailyStep>)
    fun getTempDailySteps(): Flow<List<DailyStep>>
}