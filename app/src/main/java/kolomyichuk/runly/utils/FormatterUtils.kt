package kolomyichuk.runly.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatterUtils {

    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long, includeMillis: Boolean = false): String {
        var milliseconds = millis
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        milliseconds -= hours * 1000 * 60 * 60
        val minutes = (milliseconds / (1000 * 60)).toInt()
        milliseconds -= minutes * 1000 * 60
        val seconds = (milliseconds / 1000).toInt()
        milliseconds -= seconds * 1000

        return if (includeMillis) {
            val millisSec = milliseconds / 10
            String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, millisSec)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    fun formatDistanceToKm(distanceInMeters: Double): String {
        return "%.2f".format(distanceInMeters / 1000)
    }

    fun Long.toFormattedDateTime(): String {
        val date = Date(this)
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }
}