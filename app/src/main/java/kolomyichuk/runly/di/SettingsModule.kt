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
import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.repository.SettingsRepositoryImpl
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import javax.inject.Singleton

private const val SETTINGS_DATA_STORE_NAME = "settings.preferences_pb"

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile(SETTINGS_DATA_STORE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        preferencesDataStore: SettingsPreferencesDataStore
    ): SettingsRepository {
        return SettingsRepositoryImpl(preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideSettingsPreferencesDataStore(
        dataStore: DataStore<Preferences>
    ): SettingsPreferencesDataStore {
        return SettingsPreferencesDataStore(dataStore)
    }
}