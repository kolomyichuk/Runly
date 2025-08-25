package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RunUseCaseSingletonModule {

    @Provides
    @Singleton
    fun provideGetRunDisplayModelUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository
    ): GetRunDisplayModelUseCase = GetRunDisplayModelUseCase(
        runRepository = runRepository,
        settingsRepository = settingsRepository
    )
}
