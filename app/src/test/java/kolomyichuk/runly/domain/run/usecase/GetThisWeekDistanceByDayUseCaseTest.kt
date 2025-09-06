package kolomyichuk.runly.domain.run.usecase

import io.mockk.mockk
import kolomyichuk.runly.data.repository.RunRemoteRepositoryImpl
import kolomyichuk.runly.data.repository.SettingsRepositoryImpl
import org.junit.Before

class GetThisWeekDistanceByDayUseCaseTest {
    private lateinit var runRemoteRepositoryImpl: RunRemoteRepositoryImpl
    private lateinit var settingsRepositoryImpl: SettingsRepositoryImpl
    private lateinit var useCase: GetThisWeekDistanceByDayUseCase

    @Before
    fun setup() {
        runRemoteRepositoryImpl = mockk()
        settingsRepositoryImpl = mockk()
        useCase = GetThisWeekDistanceByDayUseCase(runRemoteRepositoryImpl, settingsRepositoryImpl)
    }
}
