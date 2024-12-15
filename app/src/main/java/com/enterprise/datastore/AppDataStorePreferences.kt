package com.enterprise.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppDataStorePreferences {

    companion object {

        val PREFERENCES_NAME = "app_datastore_preferences"

        val Context.appDataStorePreferences: DataStore<Preferences> by preferencesDataStore(
            PREFERENCES_NAME
        )

        suspend fun readStringFromDataStore(context: Context, key: String): String? {

            val myKey = stringPreferencesKey(key)

            val preferences = context.appDataStorePreferences.data.first()

            //return preferences[myKey] ?: "Default Value"
            return preferences[myKey] ?: null

        }

        suspend fun writeStringToDataStore(context: Context, key: String, value: String) {

            val myKey = stringPreferencesKey(key)

            context.appDataStorePreferences.edit { preferences ->
                preferences[myKey] = value
            }

        }
    }
}