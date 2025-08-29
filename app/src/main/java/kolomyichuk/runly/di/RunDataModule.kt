package kolomyichuk.runly.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.data.local.room.AppDatabase
import kolomyichuk.runly.data.local.room.MIGRATION_1_2
import kolomyichuk.runly.data.local.room.dao.RunDao
import kolomyichuk.runly.data.repository.LocalRunRepositoryImpl
import kolomyichuk.runly.data.repository.RemoteRunRepositoryImpl
import kolomyichuk.runly.domain.run.repository.LocalRunRepository
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RunDataModule {

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
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRemoteRunRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): RemoteRunRepository {
        return RemoteRunRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideLocalRunRepository(
        runDao: RunDao
    ): LocalRunRepository {
        return LocalRunRepositoryImpl(runDao)
    }
}
