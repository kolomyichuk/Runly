package kolomyichuk.runly.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.R
import kolomyichuk.runly.utils.Constants
import kolomyichuk.runly.utils.NotificationHelper
import kolomyichuk.runly.utils.TrackingUtility
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        val timeInMillis = MutableStateFlow<Long>(0)
        val isTracking = MutableStateFlow(false)
        val isPause = MutableStateFlow(false)
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(
            Constants.TRACKING_CHANNEL_ID,
            Constants.TRACKING_CHANNEL_NAME
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when (it?.action) {
                Constants.ACTION_START_TRACKING_SERVICE -> startTracking()
                Constants.ACTION_PAUSE_TRACKING_SERVICE -> pauseTracking()
                Constants.ACTION_RESUME_TRACKING_SERVICE -> resumeTracking()
                Constants.ACTION_STOP_TRACKING_SERVICE -> stopTracking()
            }
        }
        return START_STICKY
    }

    private fun startTracking() {
        isTracking.value = (true)
        isPause.value = false
        timeRun = 0
        timeInMillis.value = 0L
        startTime = System.currentTimeMillis()

        val notification = notificationHelper.getNotification(
            getString(R.string.run),
            TrackingUtility.formatTime(timeInMillis.value)
        )
        startForeground(Constants.TRACKING_NOTIFICATION_ID, notification)
        startTimer()
    }

    private fun pauseTracking() {
        isTracking.value = false
        isPause.value = true
        timeRun += System.currentTimeMillis() - startTime
        stopTimer()
        notificationHelper.updateNotification(
            Constants.TRACKING_NOTIFICATION_ID,
            "Paused",
            TrackingUtility.formatTime(timeInMillis.value)
        )
    }

    private fun resumeTracking() {
        if (!isTracking.value) {
            isTracking.value = true
            isPause.value = false
            startTime = System.currentTimeMillis()
            startTimer()
        }
    }

    private fun stopTracking() {
        isTracking.value = false
        isPause.value = false
        timeRun = 0
        timeInMillis.value = 0L
        stopTimer()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startTimer() {
        if (timerJob != null) return

        timerJob = serviceScope.launch(CoroutineExceptionHandler { _, trowable ->
            Timber.e("Timer error: ${trowable.localizedMessage}")
        }) {
            while (isTracking.value) {
                delay(Constants.TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                timeInMillis.value = timeRun + (currentTime - startTime)

                notificationHelper.updateNotification(
                    Constants.TRACKING_NOTIFICATION_ID,
                    getString(R.string.run),
                    TrackingUtility.formatTime(timeInMillis.value)
                )
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}