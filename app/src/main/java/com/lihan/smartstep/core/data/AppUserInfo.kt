package com.lihan.smartstep.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.onboarding.presentation.model.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
        private val IS_SHOWN_BACKGROUND_ACCESS = booleanPreferencesKey("background_access")
        private val HEIGHT_UNIT = stringPreferencesKey("height_unit")
        private val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        private val INITIAL_STEPS = longPreferencesKey("initial_steps")
        private val TODAY_STEPS = longPreferencesKey("today_steps")
    }

    private val Context.datStore: DataStore<Preferences> by preferencesDataStore(name = "userInfo")

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
            preferences[HEIGHT_INT]?:175
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
           preferences[WEIGHT_INT]?:65
       }
    }

    override suspend fun updateHeightUnit(unitType: UnitType) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[HEIGHT_UNIT] = unitType.toKey()
            }
        }
    }

    override fun getHeightUnit(): Flow<UnitType> {
        return context.datStore.data.map { preferences ->
            UnitType.fromKey(preferences[HEIGHT_UNIT]?:"CM")
        }
    }


    override suspend fun updateWeightUnit(unitType: UnitType) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[WEIGHT_UNIT] = unitType.toKey()
            }
        }
    }

    override fun getWeightUnit(): Flow<UnitType> {
        return context.datStore.data.map { preferences ->
            UnitType.fromKey(preferences[WEIGHT_UNIT]?:"KG")
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

    override suspend fun updateIsShownBackgroundAccess(isShown: Boolean) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[IS_SHOWN_BACKGROUND_ACCESS] = isShown
            }
        }
    }

    override fun getShownBackgroundAccess(): Flow<Boolean> {
        return context.datStore.data.map { preferences ->
            preferences[IS_SHOWN_BACKGROUND_ACCESS]?:false
        }
    }

    override suspend fun updateDeviceInitSteps(value: Long) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[INITIAL_STEPS] = value
            }
        }
    }

    override fun getDeviceInitSteps(): Flow<Long> {
        return context.datStore.data.map { preferences ->
            preferences[INITIAL_STEPS]?:0L
        }
    }

    override suspend fun updateTodaySteps(value: Long) {
        context.datStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[TODAY_STEPS] = value
            }
        }
    }

    override fun getTodaySteps(): Flow<Long> {
        return context.datStore.data.map { preferences ->
            preferences[TODAY_STEPS]?:0L
        }
    }
}