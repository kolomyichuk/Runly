package kolomyichuk.runly.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.data.local.datastore.ThemePreferencesDataStore
import kolomyichuk.runly.data.repository.ThemeRepository
import javax.inject.Singleton

private const val THEME_DATA_STORE_NAME = "theme.preferences_pb"

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    @ThemePreferences
    fun provideThemeDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile(THEME_DATA_STORE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideThemeRepository(
        preferencesDataStore: ThemePreferencesDataStore
    ): ThemeRepository {
        return ThemeRepository(preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideThemePreferencesDataStore(
        @ThemePreferences dataStore: DataStore<Preferences>
    ): ThemePreferencesDataStore {
        return ThemePreferencesDataStore(dataStore)
    }
}