package kolomyichuk.runly.service.manager

import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TIMER_INTERVAL = 1000L

class RunTimerManager(
    private val remoteRunRepository: RemoteRunRepository
) {

    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null

    fun initializeTimer() {
        startTime = System.currentTimeMillis()
    }

    fun startTimer(scope: CoroutineScope) {
        if (timerJob != null) return

        timerJob = scope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e("Timer error: ${throwable.localizedMessage}")
        }) {
            while (remoteRunRepository.runState.value.isTracking) {
                delay(TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                val updatedTime = timeRun + (currentTime - startTime)

                remoteRunRepository.updateRunState { copy(timeInMillis = updatedTime) }
            }
        }
    }

    fun pauseTimer() {
        timeRun += System.currentTimeMillis() - startTime
        cancelTimer()
    }

    fun resumeTimer(scope: CoroutineScope) {
        startTime = System.currentTimeMillis()
        startTimer(scope)
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
