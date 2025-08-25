package kolomyichuk.runly.domain.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.repository.RunRepository
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kolomyichuk.runly.utils.FormatterUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class GetRunDisplayModelUseCaseTest {
    private lateinit var runRepository: RunRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: GetRunDisplayModelUseCase

    @Before
    fun setUp() {
        runRepository = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        useCase = GetRunDisplayModelUseCase(runRepository, settingsRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun `Given runState and distance unit When invoke is called Then emits correct RunDisplayModel`() =
        runTest {
            // Given
            val fakeRunState = RunState(
                distanceInMeters = 5000.0,
                isTracking = true,
                isPause = false,
                isActiveRun = true,
                timeInMillis = 1800000L,
                pathPoints = emptyList(),
            )

            coEvery { runRepository.runState } returns MutableStateFlow(fakeRunState)
            coEvery { settingsRepository.getDistanceUnit() } returns flowOf(DistanceUnit.KILOMETERS)

            val distance = RunCalculations.convertDistance(
                fakeRunState.distanceInMeters,
                DistanceUnit.KILOMETERS
            )
            val expected = RunDisplayModel(
                distance = String.format(Locale.US, "%.2f", distance),
                duration = FormatterUtils.formatTime(fakeRunState.timeInMillis),
                avgSpeed = RunCalculations.calculateAvgSpeed(
                    distance,
                    fakeRunState.timeInMillis
                ),
                routePoints = fakeRunState.pathPoints,
                unit = DistanceUnit.KILOMETERS,
                isActiveRun = fakeRunState.isActiveRun,
                isPause = fakeRunState.isPause,
                isTracking = fakeRunState.isTracking,
                distanceInMeters = fakeRunState.distanceInMeters,
                timeInMillis = fakeRunState.timeInMillis
            )

            // When
            val result = useCase.invoke().take(1).toList()

            // Then
            assertEquals(listOf(expected), result)
        }
}
