package kolomyichuk.runly.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatterUtils {

    private const val MILLIS_IN_SECOND = 1000
    private const val SECONDS_IN_MINUTE = 60
    private const val MINUTES_IN_HOUR = 60
    private const val MILLIS_PER_HUNDREDTH = 10

    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long, includeMillis: Boolean = false): String {
        var milliseconds = millis
        val hours =
            (milliseconds / (MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR)).toInt()
        milliseconds -= hours * MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR
        val minutes = (milliseconds / (MILLIS_IN_SECOND * SECONDS_IN_MINUTE)).toInt()
        milliseconds -= minutes * MILLIS_IN_SECOND * SECONDS_IN_MINUTE
        val seconds = (milliseconds / MILLIS_IN_SECOND).toInt()
        milliseconds -= seconds * MILLIS_IN_SECOND

        return if (includeMillis) {
            val millisSec = milliseconds / MILLIS_PER_HUNDREDTH
            String.format(Locale.US, "%02d:%02d:%02d:%02d", hours, minutes, seconds, millisSec)
        } else {
            String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    fun Long.toFormattedDateTime(): String {
        val date = Date(this)
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }
}
