package com.example.workpulse.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.cookieDataStore by preferencesDataStore("cookie_store")

@Singleton
class CookieStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    companion object {
        private val SID = stringPreferencesKey("sid")
    }

    suspend fun saveSid(sid: String) {
        context.cookieDataStore.edit {
            it[SID] = sid
        }
    }

    suspend fun getSid(): String {
        return context.cookieDataStore.data.first()[SID] ?: ""
    }

    suspend fun clear() {
        context.cookieDataStore.edit {
            it.remove(SID)
        }
    }
}