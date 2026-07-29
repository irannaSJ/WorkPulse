package com.example.workpulse.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.workpulse.feature.profile.ProfileUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {

        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        private val EMPLOYEE_ID = stringPreferencesKey("employee_id")

        private val USER_ID = stringPreferencesKey("user_id")

        private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")

        private val APP_UUID = stringPreferencesKey("app_uuid")
        private val COMPANY_NAME = stringPreferencesKey("company_name")
    }

    suspend fun saveSession(
        employeeId: String,
        userId: String,
        company: String?
    ) {

        context.dataStore.edit { preferences ->

            preferences[IS_LOGGED_IN] = true

            preferences[EMPLOYEE_ID] = employeeId

            preferences[USER_ID] = userId
            preferences[COMPANY_NAME] = company as String
        }
    }

    suspend fun updateLastSyncTime(time: Long) {

        context.dataStore.edit {

            it[LAST_SYNC_TIME] = time
        }
    }

    suspend fun isLoggedIn(): Boolean {

        return context.dataStore.data

            .catch {

                if (it is IOException) {

                    emit(emptyPreferences())

                } else {

                    throw it
                }
            }

            .first()[IS_LOGGED_IN] ?: false
    }

    suspend fun getEmployeeId(): String {

        return context.dataStore.data

            .first()[EMPLOYEE_ID] ?: ""
    }

    suspend fun getCompanyName() : String{
        return context.dataStore.data
            .first()[COMPANY_NAME]?: ""
    }

    suspend fun getUserId(): String {

        return context.dataStore.data

            .first()[USER_ID] ?: ""
    }

    suspend fun getLastSyncTime(): Long {

        return context.dataStore.data

            .first()[LAST_SYNC_TIME] ?: 0L
    }

    suspend fun getOrCreateAppUuid(): String {

        val preferences = context.dataStore.data.first()

        val existingUuid = preferences[APP_UUID]

        if (!existingUuid.isNullOrEmpty()) {
            return existingUuid
        }

        val newUuid = UUID.randomUUID().toString()



        context.dataStore.edit { prefs ->
            prefs[APP_UUID] = newUuid
        }

        return newUuid
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data

        .catch {

            if (it is IOException) {

                emit(emptyPreferences())

            } else {

                throw it
            }
        }

        .map {

            it[IS_LOGGED_IN] ?: false
        }

    val employeeIdFlow: Flow<String> = context.dataStore.data

        .catch {

            if (it is IOException) {

                emit(emptyPreferences())

            } else {

                throw it
            }
        }

        .map {

            it[EMPLOYEE_ID] ?: ""
        }

    val userIdFlow: Flow<String> = context.dataStore.data

        .catch {

            if (it is IOException) {

                emit(emptyPreferences())

            } else {

                throw it
            }
        }

        .map {

            it[USER_ID] ?: ""
        }

    suspend fun clearSession() {

        context.dataStore.edit {

            it.clear()
        }
    }
}