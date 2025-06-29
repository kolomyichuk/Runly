package kolomyichuk.runly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.data.local.datastore.SettingsPreferencesDataStore
import kolomyichuk.runly.data.local.room.AppDatabase
import kolomyichuk.runly.data.local.room.MIGRATION_1_2
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.repository.RunRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RunModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideRunDao(db: AppDatabase): RunDao {
        return db.getRunDao()
    }

    @Provides
    @Singleton
    fun provideRunRepository(
        runDao: RunDao,
        settingsDataStore: SettingsPreferencesDataStore
    ): RunRepository {
        return RunRepository(runDao, settingsDataStore)
    }
}