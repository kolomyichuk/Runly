package kolomyichuk.runly.domain.run.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kolomyichuk.runly.domain.run.RunCalculations
import kolomyichuk.runly.domain.run.model.RunChart
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class GetThisWeekDistanceByDayUseCaseTest {
    private lateinit var runRemoteRepository: RunRemoteRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: GetThisWeekDistanceByDayUseCase

    @Before
    fun setup() {
        runRemoteRepository = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        useCase = GetThisWeekDistanceByDayUseCase(runRemoteRepository, settingsRepository)
        mockkObject(RunCalculations)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given runs for some days of the week, When invoke is called, Then returns list of distances for all 7 days with correct conversions`() = runTest {
        // Given
        val unit = DistanceUnit.KILOMETERS
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY).atStartOfDay(zoneId).toInstant()

        val runChartData = listOf(
            createRunChart(monday.plus(Duration.ofDays(0)), 1000.0),
            createRunChart(monday.plus(Duration.ofDays(1)), 2000.0),
            createRunChart(monday.plus(Duration.ofDays(4)), 3000.0),
        )

        coEvery { settingsRepository.getDistanceUnit() } returns flowOf(unit)
        coEvery { runRemoteRepository.getThisWeekDistanceByDay(any(), any()) } returns runChartData
        every { RunCalculations.convertDistance(any(), unit) } answers { firstArg<Double>() / 1000 }

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(7, result.size)

        val expected = listOf(1f, 2f, 0f, 0f, 3f, 0f, 0f)

        assertEquals(expected, result)

        coVerify(exactly = 1) { settingsRepository.getDistanceUnit() }
        coVerify(exactly = 1) { runRemoteRepository.getThisWeekDistanceByDay(any(), any()) }
        verify(atLeast = 1) { RunCalculations.convertDistance(any(), unit) }
    }

    private fun createRunChart(instant: Instant, meters: Double): RunChart {
        return RunChart(
            timestamp = instant.toEpochMilli(),
            distanceMeters = meters.toFloat()
        )
    }
}
