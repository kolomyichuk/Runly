package kolomyichuk.runly.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.service.manager.RunLocationManager
import kolomyichuk.runly.service.manager.RunNotificationManager
import kolomyichuk.runly.service.manager.RunTimerManager
import kolomyichuk.runly.utils.FormatterUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    @Inject
    lateinit var runNotificationManager: RunNotificationManager

    @Inject
    lateinit var runTimerManager: RunTimerManager

    @Inject
    lateinit var runLocationManager: RunLocationManager

    @Inject
    lateinit var getRunDisplayModelUseCase: GetRunDisplayModelUseCase

    @Inject
    lateinit var remoteRunRepository: RemoteRunRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        runNotificationManager.createNotificationChannel(
            TRACKING_CHANNEL_ID,
            TRACKING_CHANNEL_NAME
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when (it?.action) {
                ACTION_START_TRACKING -> startTracking()
                ACTION_PAUSE_TRACKING -> pauseTracking()
                ACTION_RESUME_TRACKING -> resumeTracking()
                ACTION_STOP_TRACKING -> stopTracking()
            }
        }
        return START_STICKY
    }

    private fun startTracking() {
        remoteRunRepository.updateRunState {
            copy(isTracking = true, isActiveRun = true, isPause = false)
        }
        val currentTime = remoteRunRepository.runState.value.timeInMillis
        val notification = runNotificationManager.getNotification(
            getString(R.string.run),
            FormatterUtils.formatTime(currentTime)
        )

        startForeground(TRACKING_NOTIFICATION_ID, notification)
        runTimerManager.initializeTimer()
        serviceScope.launch {
            runTimerManager.startTimer(this)
        }
        serviceScope.launch {
            runNotificationManager.startNotificationUpdater(this)
        }
        runLocationManager.startLocationTracking()
    }

    private fun pauseTracking() {
        remoteRunRepository.updateRunState { copy(isTracking = false, isPause = true) }
        runTimerManager.pauseTimer()
        runLocationManager.stopLocationTracking()
    }

    private fun resumeTracking() {
        if (!remoteRunRepository.runState.value.isTracking) {
            remoteRunRepository.updateRunState {
                val updatedPath = remoteRunRepository.runState.value.pathPoints.toMutableList()
                updatedPath.add(emptyList())
                copy(isTracking = true, isPause = false, pathPoints = updatedPath.toList())
            }
            serviceScope.launch {
                runTimerManager.resumeTimer(this)
            }
            runLocationManager.startLocationTracking()
        }
    }

    private fun stopTracking() {
        remoteRunRepository.updateRunState { RunState() }
        runTimerManager.stopTimer()
        runLocationManager.stopLocationTracking()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        runLocationManager.stopLocationTracking()
        runTimerManager.stopTimer()
        runNotificationManager.stopNotificationUpdater()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_START_TRACKING = "ACTION_START_SERVICE"
        const val ACTION_RESUME_TRACKING = "ACTION_RESUME_SERVICE"
        const val ACTION_PAUSE_TRACKING = "ACTION_PAUSE_SERVICE"
        const val ACTION_STOP_TRACKING = "ACTION_STOP_SERVICE"

        const val TRACKING_NOTIFICATION_ID = 1
        const val TRACKING_CHANNEL_ID = "ch-1"
        private const val TRACKING_CHANNEL_NAME = "run"
    }
}
