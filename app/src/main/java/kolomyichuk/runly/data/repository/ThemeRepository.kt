package kolomyichuk.runly.data.repository

import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.data.local.datastore.ThemePreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// TODO @Singleton annotation and @Inject constructor annotation is redundant here
// TODO You already provided this class in DatabaseModule
@Singleton
class ThemeRepository @Inject constructor(
    private val preferencesDataStore: ThemePreferencesDataStore
) {
    suspend fun saveTheme(theme: AppTheme){
        preferencesDataStore.saveTheme(theme)
    }

    val themeFlow: Flow<AppTheme> = preferencesDataStore.themeFlow
}



