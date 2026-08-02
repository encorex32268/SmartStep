package com.lihan.smartstep.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.data.model.UserData
import com.lihan.smartstep.core.data.model.WeightUnit
import com.lihan.smartstep.core.domain.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DefaultUserDataStore(
    private val context: Context
): UserDataStore {

    companion object{
        private val USER_DATA_KEY = stringPreferencesKey("user_data")
        private val BACKGROUND_ACCESS_KEY = booleanPreferencesKey("isShownBackgroundAccess")
        private val STEP_GOAL_KEY = intPreferencesKey("step_goal")
        private val TODAY_STEPS_KEY = intPreferencesKey("today_steps")
        private val IS_TRACKING_KEY = booleanPreferencesKey("isTracking")
        private val TRACKING_TIME_KEY = longPreferencesKey("tracking_time")
    }

    override suspend fun setUserData(data: UserData) {
        context.datastore.edit { preferences ->
            preferences[USER_DATA_KEY] = Json.encodeToString(data)
        }
    }

    override val userData: Flow<UserData?>
        get() = context.datastore.data.map { preferences ->
            val jsonString = preferences[USER_DATA_KEY]
            if (!jsonString.isNullOrEmpty()) {
                Json.decodeFromString(jsonString)
            } else {
                UserData(
                    gender = Gender.Female.name,
                    height = "175",
                    heightUnit = "cm",
                    weight = "65",
                    weightUnit = "kg"
                )
            }
        }

    override suspend fun setIsShownBackgroundAccess(value: Boolean) {
       context.datastore.edit { preferences ->
           preferences[BACKGROUND_ACCESS_KEY] = value
       }
    }

    override val isShownBackgroundAccess: Flow<Boolean>
        get() = context.datastore.data.map { preferences ->
            preferences[BACKGROUND_ACCESS_KEY]?:false
        }

    override suspend fun setStepGoal(steps: Int) {
        context.datastore.edit { preferences ->
            preferences[STEP_GOAL_KEY] = steps
        }
    }

    override val stepGoal: Flow<Int>
        get() = context.datastore.data.map { preferences ->
            preferences[STEP_GOAL_KEY]?:2000
        }

    override suspend fun setTodaySteps(steps: Int) {
        context.datastore.edit { preferences ->
            preferences[TODAY_STEPS_KEY] = steps
        }
    }

    override val todaySteps: Flow<Int>
        get() = context.datastore.data.map { preferences ->
            preferences[TODAY_STEPS_KEY]?:0
        }

    override suspend fun setIsTracking(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[IS_TRACKING_KEY] = value
        }
    }

    override val isTracking: Flow<Boolean>
        get() = context.datastore.data.map { preferences ->
            preferences[IS_TRACKING_KEY]?:false
        }

    override suspend fun setTrackingTime(time: Long) {
        context.datastore.edit { preferences ->
            preferences[TRACKING_TIME_KEY] = time
        }
    }

    override val trackingTime: Flow<Long>
        get() = context.datastore.data.map { preferences ->
            preferences[TRACKING_TIME_KEY] ?: 0L
        }

    override suspend fun cleanStepsData() {
        context.datastore.edit { preferences ->
            preferences[TRACKING_TIME_KEY] = 0L
            preferences[TODAY_STEPS_KEY] = 0
        }
    }

}