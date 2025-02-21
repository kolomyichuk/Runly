package kolomyichuk.runly.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat.Builder
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val notificationManager: NotificationManager,
    private val trackingNotificationBuilder: Builder
) {
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
            .setContentText(content)
            .build()
    }

    fun updateNotification(notificationId: Int, title: String, content: String) {
        val notification = getNotification(title,content)
        notificationManager.notify(notificationId, notification)
    }
}