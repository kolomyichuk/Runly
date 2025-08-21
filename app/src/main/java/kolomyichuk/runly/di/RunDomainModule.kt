package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.usecase.DeleteRunByIdInFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetAllRunsFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunByIdFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.run.usecase.InsertRunInFirestoreUseCase
import kolomyichuk.runly.domain.settings.repository.SettingsRepository

@Module
@InstallIn(ViewModelComponent::class)
object RunDomainModule {

    @Provides
    fun provideDeleteRunByIdInFirestoreUseCase(
        repository: RunRepository
    ): DeleteRunByIdInFirestoreUseCase = DeleteRunByIdInFirestoreUseCase(
        runRepository = repository
    )

    @Provides
    fun provideGetAllRunsFromFirestoreUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository
    ): GetAllRunsFromFirestoreUseCase = GetAllRunsFromFirestoreUseCase(
        runRepository = runRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunByIdFromFirestoreUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository
    ): GetRunByIdFromFirestoreUseCase = GetRunByIdFromFirestoreUseCase(
        runRepository = runRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunDisplayModelUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository
    ): GetRunDisplayModelUseCase = GetRunDisplayModelUseCase(
        runRepository = runRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideInsertRunInFirestoreUseCase(
        runRepository: RunRepository
    ): InsertRunInFirestoreUseCase = InsertRunInFirestoreUseCase(
        runRepository = runRepository
    )

}
