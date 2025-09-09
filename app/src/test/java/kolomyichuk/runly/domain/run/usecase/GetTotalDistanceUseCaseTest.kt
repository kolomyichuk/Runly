package kolomyichuk.runly.domain.run.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTotalDistanceUseCaseTest {
    private lateinit var runRemoteRepository: RunRemoteRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: GetTotalDistanceUseCase

    @Before
    fun setup() {
        runRemoteRepository = mockk()
        settingsRepository = mockk()
        useCase = GetTotalDistanceUseCase(runRemoteRepository, settingsRepository)
        mockkObject(RunCalculations)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given total distance in meters and unit is Km, When invoke is called, Then returns formatted distance in kilometers`() =
        runTest {
            // Given
            val distanceInMeters = 3000.0
            val unit = DistanceUnit.KILOMETERS
            val expectedConverted = 3.0
            val expectedFormatted = 3.00f

            coEvery { runRemoteRepository.getTotalDistance() } returns distanceInMeters
            every { settingsRepository.getDistanceUnit() } returns flowOf(unit)
            every {
                RunCalculations.convertDistance(distanceInMeters, unit)
            } returns expectedConverted

            // When
            val result = useCase.invoke()

            // Then
            assertEquals(expectedFormatted, result, 0.001f)
            coVerify(exactly = 1) { runRemoteRepository.getTotalDistance() }
            verify(exactly = 1) { settingsRepository.getDistanceUnit() }
            verify(exactly = 1) { RunCalculations.convertDistance(distanceInMeters, unit) }
        }

    @Test
    fun `Given total distance in meters and unit is Miles, When invoke is called, Then returns formatted distance in miles`() =
        runTest {
            // Given
            val distanceInMeters = 1609.34
            val unit = DistanceUnit.MILES
            val expectedConverted = 1.0
            val expectedFormatted = 1.00f

            coEvery { runRemoteRepository.getTotalDistance() } returns distanceInMeters
            every { settingsRepository.getDistanceUnit() } returns flowOf(unit)
            every {
                RunCalculations.convertDistance(distanceInMeters, unit)
            } returns expectedConverted

            // When
            val result = useCase.invoke()

            // Then
            assertEquals(expectedFormatted, result, 0.001f)
            coVerify(exactly = 1) { runRemoteRepository.getTotalDistance() }
            verify(exactly = 1) { settingsRepository.getDistanceUnit() }
            verify(exactly = 1) { RunCalculations.convertDistance(distanceInMeters, unit) }
        }
}
