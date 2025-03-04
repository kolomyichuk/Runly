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
import kolomyichuk.runly.data.local.datastore.ProfilePreferencesDataStore
import kolomyichuk.runly.data.local.storage.ImageStorage
import kolomyichuk.runly.data.repository.ProfileRepository
import kolomyichuk.runly.data.repository.ThemeRepository
import kolomyichuk.runly.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ThemePreferences
    fun provideThemeDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile(Constants.THEME_DATA_STORE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideThemeRepository(
        @ThemePreferences dataStore: DataStore<Preferences>
    ): ThemeRepository {
        return ThemeRepository(dataStore)
    }

    @Provides
    @Singleton
    @ProfilePreferences
    fun provideProfileDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile("profile.preferences_pb") }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ProfilePreferences dataStore: DataStore<Preferences>
    ): ProfilePreferencesDataStore {
        return ProfilePreferencesDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideImageStorage(@ApplicationContext context: Context): ImageStorage {
        return ImageStorage(context)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        preferencesDataStore: ProfilePreferencesDataStore,
        imageStorage: ImageStorage
    ): ProfileRepository {
        return ProfileRepository(preferencesDataStore, imageStorage)
    }
}