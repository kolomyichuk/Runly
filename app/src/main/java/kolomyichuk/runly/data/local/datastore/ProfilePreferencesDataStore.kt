package kolomyichuk.runly.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kolomyichuk.runly.di.ProfilePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfilePreferencesDataStore @Inject constructor(
    @ProfilePreferences private val dataStore: DataStore<Preferences>
) {

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    val username: Flow<String> = dataStore.data.map { it[USERNAME_KEY] ?: "username" }

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
    }
}
