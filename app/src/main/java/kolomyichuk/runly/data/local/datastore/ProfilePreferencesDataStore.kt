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

// TODO @Inject constructor annotation is redundant here
// TODO You already provided this class in DatabaseModule
class ProfilePreferencesDataStore @Inject constructor(
    @ProfilePreferences private val dataStore: DataStore<Preferences>
) {

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_USERNAME_KEY] = username
        }
    }

    val username: Flow<String> = dataStore.data.map { it[PROFILE_USERNAME_KEY] ?: "@username" }

    suspend fun saveProfileImageFileName(fileName: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_IMAGE_KEY] = fileName
        }
    }

    suspend fun getProfileImageFileName(): String? {
        val prefs = dataStore.data.first()
        return prefs[PROFILE_IMAGE_KEY]
    }

    companion object {
        private val PROFILE_USERNAME_KEY = stringPreferencesKey("profile_username_key")
        private val PROFILE_IMAGE_KEY = stringPreferencesKey("profile_image_key")
    }
}
