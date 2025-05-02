package kolomyichuk.runly.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.data.local.datastore.ProfilePreferencesDataStore
import kolomyichuk.runly.data.local.datastore.ThemePreferencesDataStore
import kolomyichuk.runly.data.local.room.AppDatabase
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.local.storage.ImageStorage
import kolomyichuk.runly.data.repository.ProfileRepository
import kolomyichuk.runly.data.repository.RunRepository
import kolomyichuk.runly.data.repository.ThemeRepository
import javax.inject.Singleton

private const val PROFILE_DATA_STORE_NAME = "profile.preferences_pb"
private const val THEME_DATA_STORE_NAME = "theme.preferences_pb"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRunDao(db: AppDatabase): RunDao {
        return db.getRunDao()
    }

    @Provides
    @Singleton
    fun provideRunRepository(runDao: RunDao): RunRepository {
        return RunRepository(runDao)
    }

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


    @Provides
    @Singleton
    @ProfilePreferences
    fun provideProfileDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile(PROFILE_DATA_STORE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideProfilePreferencesDataStore(
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