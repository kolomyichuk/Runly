package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RunUseCaseSingletonModule {

    @Provides
    @Singleton
    fun provideGetRunDisplayModelUseCase(
        remoteRunRepository: RemoteRunRepository,
        settingsRepository: SettingsRepository
    ): GetRunDisplayModelUseCase = GetRunDisplayModelUseCase(
        remoteRunRepository = remoteRunRepository,
        settingsRepository = settingsRepository
    )
}
