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
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

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
        private val TODAY_TIMER = longPreferencesKey("mill_seconds")
        private val TODAY_STEPS = longPreferencesKey("today_steps")
        private val DAILY_STEPS = stringPreferencesKey("daily_steps")
        private val INITIAL_STEPS = longPreferencesKey("initial_steps")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userInfo")

    override suspend fun updateGender(gender: Gender) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[GENDER_INT] = when(gender){
                    Gender.FEMALE -> 0
                    Gender.MALE -> 1
                }
            }
        }
    }

    override fun getGender(): Flow<Gender> {
        return context.dataStore.data.map { preferences ->
            when(preferences[GENDER_INT]?:0){
                0 -> Gender.FEMALE
                1 -> Gender.MALE
                else -> Gender.FEMALE
            }
        }
    }

    override suspend fun updateHeight(height: Int) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[HEIGHT_INT] = height
            }
        }
    }

    override fun getHeight(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[HEIGHT_INT]?:175
        }
    }

    override suspend fun updateWeight(weight: Int) {
       context.dataStore.updateData { preferences ->
           preferences.toMutablePreferences().also { mutablePreferences ->
               mutablePreferences[WEIGHT_INT] = weight
           }
       }
    }

    override fun getWeight(): Flow<Int> {
       return context.dataStore.data.map { preferences ->
           preferences[WEIGHT_INT]?:65
       }
    }

    override suspend fun updateHeightUnit(unitType: UnitType) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[HEIGHT_UNIT] = unitType.toKey()
            }
        }
    }

    override fun getHeightUnit(): Flow<UnitType> {
        return context.dataStore.data.map { preferences ->
            UnitType.fromKey(preferences[HEIGHT_UNIT]?:"CM")
        }
    }


    override suspend fun updateWeightUnit(unitType: UnitType) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[WEIGHT_UNIT] = unitType.toKey()
            }
        }
    }

    override fun getWeightUnit(): Flow<UnitType> {
        return context.dataStore.data.map { preferences ->
            UnitType.fromKey(preferences[WEIGHT_UNIT]?:"KG")
        }
    }

    override suspend fun updateIsSetting(isSet: Boolean) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[IS_SET_BOOLEAN] = isSet
            }
        }
    }

    override fun getIsSetting(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_SET_BOOLEAN]?:false
        }
    }

    override suspend fun updateTotalStep(value: Long) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[TOTAL_STEP] = value
            }
        }
    }

    override fun getTotalStep(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[TOTAL_STEP]?:6000L
        }
    }

    override suspend fun updateIsShownBackgroundAccess(isShown: Boolean) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[IS_SHOWN_BACKGROUND_ACCESS] = isShown
            }
        }
    }

    override fun getShownBackgroundAccess(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_SHOWN_BACKGROUND_ACCESS]?:false
        }
    }

    override suspend fun updateTodaySteps(value: Long) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[TODAY_STEPS] = value
            }
        }
    }

    override fun getTodaySteps(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[TODAY_STEPS]?:0L
        }
    }

    override suspend fun updateTempDailySteps(dailySteps: List<DailyStep>) {
        val json = Json.encodeToString(dailySteps)
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[DAILY_STEPS] = json
            }
        }
    }

    override fun getTempDailySteps(): Flow<List<DailyStep>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[DAILY_STEPS]
            if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                Json.decodeFromString<List<DailyStep>>(json)
            }
        }
    }

    override suspend fun updateTodayTimer(millSeconds: Long) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[TODAY_TIMER] = millSeconds
            }
        }
    }

    override fun getTodayTimer(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[TODAY_TIMER]?:0L
        }
    }

    override suspend fun updateInitialSteps(value: Long) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[INITIAL_STEPS] = value
            }
        }
    }

    override fun getInitialSteps(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[INITIAL_STEPS]?:0L
        }
    }
}