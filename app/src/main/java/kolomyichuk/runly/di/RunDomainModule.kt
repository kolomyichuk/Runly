package kolomyichuk.runly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.usecase.CalculateAvgSpeedUseCase
import kolomyichuk.runly.domain.run.usecase.CalculateDistanceUseCase
import kolomyichuk.runly.domain.run.usecase.DeleteRunByIdInFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetAllRunsFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunByIdFromFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunStateUseCase
import kolomyichuk.runly.domain.run.usecase.InsertRunInFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.UpdateRunStateUseCase
import kolomyichuk.runly.domain.settings.repository.SettingsRepository

@Module
@InstallIn(ViewModelComponent::class)
object RunDomainModule {

    @Provides
    fun provideCalculateAvgSpeedUseCase(): CalculateAvgSpeedUseCase =
        CalculateAvgSpeedUseCase()

    @Provides
    fun provideCalculateDistanceUseCase(): CalculateDistanceUseCase =
        CalculateDistanceUseCase()

    @Provides
    fun provideDeleteRunByIdInFirestoreUseCase(
        repository: RunRepository
    ): DeleteRunByIdInFirestoreUseCase = DeleteRunByIdInFirestoreUseCase(
        runRepository = repository
    )

    @Provides
    fun provideGetAllRunsFromFirestoreUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository,
        calculateDistanceUseCase: CalculateDistanceUseCase,
        calculateAvgSpeedUseCase: CalculateAvgSpeedUseCase
    ): GetAllRunsFromFirestoreUseCase = GetAllRunsFromFirestoreUseCase(
        runRepository = runRepository,
        settingsRepository = settingsRepository,
        calculateDistanceUseCase = calculateDistanceUseCase,
        calculateAvgSpeedUseCase = calculateAvgSpeedUseCase
    )

    @Provides
    fun provideGetRunByIdFromFirestoreUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository,
        calculateDistanceUseCase: CalculateDistanceUseCase,
        calculateAvgSpeedUseCase: CalculateAvgSpeedUseCase
    ): GetRunByIdFromFirestoreUseCase = GetRunByIdFromFirestoreUseCase(
        runRepository = runRepository,
        calculateDistanceUseCase = calculateDistanceUseCase,
        calculateAvgSpeedUseCase = calculateAvgSpeedUseCase,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunDisplayModelUseCase(
        runRepository: RunRepository,
        settingsRepository: SettingsRepository,
        calculateDistanceUseCase: CalculateDistanceUseCase,
        calculateAvgSpeedUseCase: CalculateAvgSpeedUseCase
    ): GetRunDisplayModelUseCase = GetRunDisplayModelUseCase(
        runRepository = runRepository,
        calculateDistanceUseCase = calculateDistanceUseCase,
        calculateAvgSpeedUseCase = calculateAvgSpeedUseCase,
        settingsRepository = settingsRepository
    )

    @Provides
    fun provideGetRunStateUseCase(
        runRepository: RunRepository
    ): GetRunStateUseCase = GetRunStateUseCase(
        runRepository = runRepository
    )

    @Provides
    fun provideInsertRunInFirestoreUseCase(
        runRepository: RunRepository
    ): InsertRunInFirestoreUseCase = InsertRunInFirestoreUseCase(
        runRepository = runRepository
    )

    @Provides
    fun provideUpdateRunStateUseCase(
        runRepository: RunRepository
    ): UpdateRunStateUseCase = UpdateRunStateUseCase(
        runRepository = runRepository
    )
}