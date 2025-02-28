package kolomyichuk.runly.utils

import android.content.Context
import android.content.Intent
import kolomyichuk.runly.service.RunTrackingService

fun startRunTrackingService(context: Context) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = Constants.ACTION_START_TRACKING_SERVICE
    }
    context.startService(intent)
}

fun pauseTrackingService(context: Context) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = Constants.ACTION_PAUSE_TRACKING_SERVICE
    }
    context.startService(intent)
}

fun resumeTrackingService(context: Context) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = Constants.ACTION_RESUME_TRACKING_SERVICE
    }
    context.startService(intent)
}

fun stopTrackingService(context: Context) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = Constants.ACTION_STOP_TRACKING_SERVICE
    }
    context.startService(intent)
}