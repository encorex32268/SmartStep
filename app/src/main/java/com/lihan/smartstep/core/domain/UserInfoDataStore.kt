package com.lihan.smartstep.core.domain

import com.lihan.smartstep.onboarding.presentation.model.Gender
import kotlinx.coroutines.flow.Flow

interface UserInfoDataStore {
    suspend fun updateGender(gender: Gender)
    fun getGender(): Flow<Gender>

    suspend fun updateHeight(height: Int)
    fun getHeight(): Flow<Int>

    suspend fun updateWeight(weight: Int)
    fun getWeight(): Flow<Int>

    suspend fun updateIsSetting(isSet: Boolean)
    fun getIsSetting(): Flow<Boolean>

    suspend fun updateTotalStep(value: Long)
    fun getTotalStep(): Flow<Long>
}