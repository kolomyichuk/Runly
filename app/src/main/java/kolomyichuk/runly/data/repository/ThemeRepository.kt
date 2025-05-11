package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.data.local.datastore.ThemePreferencesDataStore
import kotlinx.coroutines.flow.Flow

class ThemeRepository (
    private val preferencesDataStore: ThemePreferencesDataStore
) {
    suspend fun saveTheme(theme: AppTheme){
        preferencesDataStore.saveTheme(theme)
    }

    val themeFlow: Flow<AppTheme> = preferencesDataStore.themeFlow
}



