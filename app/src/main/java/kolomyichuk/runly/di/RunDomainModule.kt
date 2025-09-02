package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.run.usecase.DeleteRunByIdInFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetAllRunsFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunByIdFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetThisWeekDistanceByDayUseCase
import kolomyichuk.runly.domain.run.usecase.GetTotalDistanceUseCase
import kolomyichuk.runly.domain.run.usecase.InsertRunInFirestoreUseCase
import kolomyichuk.runly.domain.settings.repository.SettingsRepository

@Module
@InstallIn(ViewModelComponent::class)
object RunDomainModule {

    @Provides
    fun provideDeleteRunByIdInFirestoreUseCase(
        repository: RunRemoteRepository
    ): DeleteRunByIdInFirestoreUseCase = DeleteRunByIdInFirestoreUseCase(
        runRemoteRepository = repository
    )

    @Provides
    fun provideGetAllRunsFromFirestoreUseCase(
        runRemoteRepository: RunRemoteRepository,
        settingsRepository: SettingsRepository
    ): GetAllRunsFromFirestoreUseCase = GetAllRunsFromFirestoreUseCase(
        runRemoteRepository = runRemoteRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunByIdFromFirestoreUseCase(
        runRemoteRepository: RunRemoteRepository,
        settingsRepository: SettingsRepository
    ): GetRunByIdFromFirestoreUseCase = GetRunByIdFromFirestoreUseCase(
        runRemoteRepository = runRemoteRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideInsertRunInFirestoreUseCase(
        runRemoteRepository: RunRemoteRepository
    ): InsertRunInFirestoreUseCase = InsertRunInFirestoreUseCase(
        runRemoteRepository = runRemoteRepository
    )

    @Provides
    fun provideGetThisWeekDistanceByDayUseCase(
        runRemoteRepository: RunRemoteRepository,
        settingsRepository: SettingsRepository
    ): GetThisWeekDistanceByDayUseCase = GetThisWeekDistanceByDayUseCase(
        runRemoteRepository = runRemoteRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetTotalDistanceUseCase(
        runRemoteRepository: RunRemoteRepository,
        settingsRepository: SettingsRepository
    ): GetTotalDistanceUseCase = GetTotalDistanceUseCase(
        runRemoteRepository = runRemoteRepository,
        settingsRepository = settingsRepository
    )

}
