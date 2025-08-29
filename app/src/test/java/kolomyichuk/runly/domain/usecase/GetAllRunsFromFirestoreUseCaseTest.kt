package kolomyichuk.runly.domain.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kolomyichuk.runly.domain.run.ext.toRunDisplayModel
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.run.usecase.GetAllRunsFromFirestoreUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kolomyichuk.runly.utils.createRun1
import kolomyichuk.runly.utils.createRun2
import kolomyichuk.runly.utils.createRunsList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllRunsFromFirestoreUseCaseTest {
    private lateinit var remoteRunRepository: RemoteRunRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: GetAllRunsFromFirestoreUseCase

    @Before
    fun setUp() {
        remoteRunRepository = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        useCase = GetAllRunsFromFirestoreUseCase(remoteRunRepository, settingsRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given empty list When no runs available Then returns empty list`() = runTest {
        // Given
        val emptyRunsList = emptyList<Run>()
        val distanceUnit = DistanceUnit.KILOMETERS

        coEvery { remoteRunRepository.getAllRunsFromFirestore() } returns flowOf(emptyRunsList)
        coEvery { settingsRepository.getDistanceUnit() } returns flowOf(distanceUnit)


        // When
        val result = useCase.invoke().first()

        // Then
        assertTrue(result.isEmpty())
        coVerify { remoteRunRepository.getAllRunsFromFirestore() }
        coVerify { settingsRepository.getDistanceUnit() }
    }

    @Test
    fun `Given runs exist and distance unit When invoke is called Then returns list of RunDisplayModels`() =
        runTest {
            // Given
            val unit = DistanceUnit.KILOMETERS
            val expected = createRunsList().map { it.toRunDisplayModel(unit) }

            coEvery { remoteRunRepository.getAllRunsFromFirestore() } returns flowOf(createRunsList())
            coEvery { settingsRepository.getDistanceUnit() } returns flowOf(unit)

            // When
            val result = useCase.invoke().first()

            // Then
            assertEquals(expected, result)
            coVerify { remoteRunRepository.getAllRunsFromFirestore() }
            coVerify { settingsRepository.getDistanceUnit() }
        }

    @Test
    fun `Given distance unit changes When invoke is called Then emits updated list of RunDisplayModels`() =
        runTest {
            // Given
            val runs = createRunsList()
            coEvery { remoteRunRepository.getAllRunsFromFirestore() } returns flowOf(createRunsList())
            coEvery { settingsRepository.getDistanceUnit() } returns flowOf(
                DistanceUnit.KILOMETERS,
                DistanceUnit.MILES
            )

            val expectedInitial = runs.map { it.toRunDisplayModel(DistanceUnit.KILOMETERS) }
            val expectedUpdated = runs.map { it.toRunDisplayModel(DistanceUnit.MILES) }

            // When
            val result = useCase.invoke().toList()

            // Then
            assertEquals(listOf(expectedInitial, expectedUpdated), result)
        }

    @Test
    fun `Given runs list changes When invoke is called Then emits updated list of RunDisplayModels`() =
        runTest {
            // Given
            val initialRuns = listOf(createRun1(), createRun2())
            val runsFlow = flowOf(initialRuns, createRunsList())
            coEvery { remoteRunRepository.getAllRunsFromFirestore() } returns runsFlow
            coEvery { settingsRepository.getDistanceUnit() } returns flowOf(DistanceUnit.KILOMETERS)

            val expectedInitial = initialRuns.map { it.toRunDisplayModel(DistanceUnit.KILOMETERS) }
            val expectedUpdated =
                createRunsList().map { it.toRunDisplayModel(DistanceUnit.KILOMETERS) }

            // When
            val results = useCase.invoke().toList()

            // Then
            assertEquals(listOf(expectedInitial, expectedUpdated), results)
        }
}
