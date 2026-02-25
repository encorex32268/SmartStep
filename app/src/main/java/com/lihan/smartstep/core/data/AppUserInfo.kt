package com.lihan.smartstep.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.onboarding.presentation.model.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppUserInfo(
    private val context: Context
): UserInfoDataStore{

    companion object{
        private val GENDER_INT = intPreferencesKey("gender")
        private val HEIGHT_INT = intPreferencesKey("height")
        private val WEIGHT_INT = intPreferencesKey("weight")
        private val IS_SET_BOOLEAN = booleanPreferencesKey("profile_setting")
        private val TOTAL_STEP = longPreferencesKey("total_step")
    }

    val Context.datStore: DataStore<Preferences> by preferencesDataStore(name = "userInfo")

    override suspend fun updateGender(gender: Gender) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[GENDER_INT] = when(gender){
                    Gender.FEMALE -> 0
                    Gender.MALE -> 1
                }
            }
        }
    }

    override fun getGender(): Flow<Gender> {
        return context.datStore.data.map { preferences ->
            when(preferences[GENDER_INT]?:0){
                0 -> Gender.FEMALE
                1 -> Gender.MALE
                else -> Gender.FEMALE
            }
        }
    }

    override suspend fun updateHeight(height: Int) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[HEIGHT_INT] = height
            }
        }
    }

    override fun getHeight(): Flow<Int> {
        return context.datStore.data.map { preferences ->
            preferences[HEIGHT_INT]?:170
        }
    }

    override suspend fun updateWeight(weight: Int) {
       context.datStore.updateData { preferences ->
           preferences.toMutablePreferences().also { mutablePreferences ->
               mutablePreferences[WEIGHT_INT] = weight
           }
       }
    }

    override fun getWeight(): Flow<Int> {
       return context.datStore.data.map { preferences ->
           preferences[WEIGHT_INT]?:60
       }
    }

    override suspend fun updateIsSetting(isSet: Boolean) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[IS_SET_BOOLEAN] = isSet
            }
        }
    }

    override fun getIsSetting(): Flow<Boolean> {
        return context.datStore.data.map { preferences ->
            preferences[IS_SET_BOOLEAN]?:false
        }
    }

    override suspend fun updateTotalStep(value: Long) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[TOTAL_STEP] = value
            }
        }
    }

    override fun getTotalStep(): Flow<Long> {
        return context.datStore.data.map { preferences ->
            preferences[TOTAL_STEP]?:6000L
        }
    }
}