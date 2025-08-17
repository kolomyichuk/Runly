package kolomyichuk.runly.service.manager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat.Builder
import dagger.hilt.android.qualifiers.ApplicationContext
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.service.RunTrackingService.Companion.TRACKING_NOTIFICATION_ID
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RunNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    private val trackingNotificationBuilder: Builder,
    private val getRunDisplayModelUseCase: GetRunDisplayModelUseCase
) : BaseServiceManager() {

    private var notificationJob: Job? = null

    @SuppressLint("ObsoleteSdkInt")
    fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getNotification(title: String, content: String): Notification {
        return trackingNotificationBuilder
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.ic_runly)
            .setContentText(content)
            .build()
    }

    fun startNotificationUpdater() {
        if (notificationJob != null) return

        notificationJob = scope.launch {
            getRunDisplayModelUseCase.invoke().collectLatest { run ->
                if (run.isTracking) {
                    val unitDistance = when (run.unit) {
                        DistanceUnit.KILOMETERS -> context.getString(R.string.km)
                        DistanceUnit.MILES -> context.getString(R.string.miles)
                    }
                    val text = "${run.duration} · ${run.distance} $unitDistance"
                    updateNotification(
                        context.getString(R.string.run),
                        text
                    )
                } else if (run.isPause) {
                    updateNotification(
                        context.getString(R.string.paused),
                        run.duration
                    )
                }
            }
        }
    }

    fun stopNotificationUpdater() {
        notificationJob?.cancel()
        notificationJob = null
        cancelScope()
    }

    private fun updateNotification(title: String, content: String) {
        val notification = getNotification(title, content)
        notificationManager.notify(TRACKING_NOTIFICATION_ID, notification)
    }
}
