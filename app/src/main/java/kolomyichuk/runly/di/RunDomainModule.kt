package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
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
        repository: RemoteRunRepository
    ): DeleteRunByIdInFirestoreUseCase = DeleteRunByIdInFirestoreUseCase(
        remoteRunRepository = repository
    )

    @Provides
    fun provideGetAllRunsFromFirestoreUseCase(
        remoteRunRepository: RemoteRunRepository,
        settingsRepository: SettingsRepository
    ): GetAllRunsFromFirestoreUseCase = GetAllRunsFromFirestoreUseCase(
        remoteRunRepository = remoteRunRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunByIdFromFirestoreUseCase(
        remoteRunRepository: RemoteRunRepository,
        settingsRepository: SettingsRepository
    ): GetRunByIdFromFirestoreUseCase = GetRunByIdFromFirestoreUseCase(
        remoteRunRepository = remoteRunRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideInsertRunInFirestoreUseCase(
        remoteRunRepository: RemoteRunRepository
    ): InsertRunInFirestoreUseCase = InsertRunInFirestoreUseCase(
        remoteRunRepository = remoteRunRepository
    )

    @Provides
    fun provideGetThisWeekDistanceByDayUseCase(
        remoteRunRepository: RemoteRunRepository,
        settingsRepository: SettingsRepository
    ): GetThisWeekDistanceByDayUseCase = GetThisWeekDistanceByDayUseCase(
        remoteRunRepository = remoteRunRepository,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetTotalDistanceUseCase(
        remoteRunRepository: RemoteRunRepository,
        settingsRepository: SettingsRepository
    ): GetTotalDistanceUseCase = GetTotalDistanceUseCase(
        remoteRunRepository = remoteRunRepository,
        settingsRepository = settingsRepository
    )

}
