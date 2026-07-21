package com.lihan.smartstep.core.domain

import com.lihan.smartstep.core.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    suspend fun setUserData(data: UserData)
    val userData: Flow<UserData?>

}