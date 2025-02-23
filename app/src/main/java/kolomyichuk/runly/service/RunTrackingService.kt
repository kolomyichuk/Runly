package kolomyichuk.runly.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.utils.Constants
import kolomyichuk.runly.utils.NotificationHelper
import kolomyichuk.runly.utils.TrackingUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private var timeInSeconds = 0L
    private var timerJob: Job? = null

    companion object {
        val runTime = MutableStateFlow<Long>(0)
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
        super.onStartCommand(intent, flags, startId)
        intent.let {
            when (it?.action) {
                Constants.ACTION_START_TRACKING_SERVICE -> {
                    startTracking()
                }

                Constants.ACTION_PAUSE_TRACKING_SERVICE -> {
                    pauseTracking()
                }

                Constants.ACTION_RESUME_TRACKING_SERVICE -> {
                    resumeTracking()
                }

                Constants.ACTION_STOP_TRACKING_SERVICE -> {
                    stopTracking()
                }
            }
        }
        return START_STICKY
    }

    private fun startTracking() {
        isTracking.value = (true)
        isPause.value = false
        timeInSeconds = 0
        runTime.value = timeInSeconds

        val notification = notificationHelper.getNotification(
            "Run",
            ""
        )
        startForeground(Constants.TRACKING_NOTIFICATION_ID, notification)
        startTimer()
    }

    private fun pauseTracking() {
        isTracking.value = false
        isPause.value = true
        stopTimer()
    }

    private fun resumeTracking() {
        if (!isTracking.value!!) {
            isTracking.value = true
            isPause.value = false
            startTimer()
        }
    }

    private fun stopTracking() {
        isTracking.value = false
        isPause.value = false
        timeInSeconds = 0
        runTime.value = timeInSeconds
        stopTimer()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                delay(1000L)
                timeInSeconds++
                runTime.value = timeInSeconds

                notificationHelper.updateNotification(
                    Constants.TRACKING_NOTIFICATION_ID,
                    "Run",
                    TrackingUtility.formatTime(timeInSeconds)
                )
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}