package kolomyichuk.runly.service.manager

import kolomyichuk.runly.domain.run.repository.RunRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val TIMER_INTERVAL = 1000L

class TimerManager @Inject constructor(
    private val runRepository: RunRepository
) : BaseServiceManager() {

    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null

    fun initializeTimer() {
        startTime = System.currentTimeMillis()
    }

    fun startTimer() {
        if (timerJob != null) return

        timerJob = scope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e("Timer error: ${throwable.localizedMessage}")
        }) {
            while (runRepository.runState.value.isTracking) {
                delay(TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                val updatedTime = timeRun + (currentTime - startTime)

                runRepository.updateRunState { copy(timeInMillis = updatedTime) }
            }
        }
    }

    fun pauseTimer() {
        timeRun += System.currentTimeMillis() - startTime
        cancelTimer()
    }

    fun resumeTimer() {
        startTime = System.currentTimeMillis()
        startTimer()
    }

    fun stopTimer() {
        timeRun = 0
        cancelTimer()
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}
