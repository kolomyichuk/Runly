package kolomyichuk.runly.utils

import android.annotation.SuppressLint

class TrackingUtility {
    companion object {
        @SuppressLint("DefaultLocale")
        fun formatTime(seconds: Long): String {
            val hrs = seconds / 3600
            val mins = (seconds % 3600) / 60
            val secs = seconds % 60
            return if (hrs > 0) {
                String.format("%d:%02d:%02d", hrs, mins, secs)
            } else {
                String.format("%d:%02d", mins, secs)
            }
        }
    }
}