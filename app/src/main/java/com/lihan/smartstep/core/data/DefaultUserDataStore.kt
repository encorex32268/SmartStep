package com.lihan.smartstep.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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

}