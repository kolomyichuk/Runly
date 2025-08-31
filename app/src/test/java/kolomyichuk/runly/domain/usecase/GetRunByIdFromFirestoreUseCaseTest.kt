package kolomyichuk.runly.domain.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kolomyichuk.runly.domain.run.ext.toRunDisplayModel
import kolomyichuk.runly.domain.run.repository.RunRemoteRepository
import kolomyichuk.runly.domain.run.usecase.GetRunByIdFromFirestoreUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kolomyichuk.runly.utils.createRun1
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRunByIdFromFirestoreUseCaseTest {
    private lateinit var runRemoteRepository: RunRemoteRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: GetRunByIdFromFirestoreUseCase

    @Before
    fun setUp() {
        runRemoteRepository = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        useCase = GetRunByIdFromFirestoreUseCase(runRemoteRepository, settingsRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given valid run and unit When invoked Then returns correct RunDisplayModel`() = runTest {
        // When
        val runId = createRun1().id
        val run = createRun1()
        val unit = DistanceUnit.KILOMETERS
        val expectedDisplayModel = run.toRunDisplayModel(unit)

        coEvery { runRemoteRepository.getRunByIdFromFirestore(runId) } returns flowOf(run)
        coEvery { settingsRepository.getDistanceUnit() } returns flowOf(unit)

        // When
        val result = useCase.invoke(runId).toList()

        // Then
        assertEquals(expectedDisplayModel, result.first())

        coVerify { runRemoteRepository.getRunByIdFromFirestore(runId) }
        coVerify { settingsRepository.getDistanceUnit() }
    }
}
