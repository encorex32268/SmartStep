package com.lihan.smartstep.core.domain

import com.lihan.smartstep.core.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    suspend fun setUserData(data: UserData)
    val userData: Flow<UserData?>

    suspend fun setIsShownBackgroundAccess(value: Boolean)
    val isShownBackgroundAccess: Flow<Boolean>

    suspend fun setStepGoal(steps: Int)
    val stepGoal: Flow<Int>

    suspend fun setTodaySteps(steps: Int)
    val todaySteps: Flow<Int>

    suspend fun setIsTracking(value: Boolean)
    val isTracking: Flow<Boolean>
}