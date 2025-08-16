package kolomyichuk.runly.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.data.repository.ProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        firebaseAuth: FirebaseAuth
    ): ProfileRepository {
        return ProfileRepository(firebaseAuth)
    }
}
