package kolomyichuk.runly.utils

object Constants {
    const val TRACKING_CHANNEL_ID = "ch-1"
    const val TRACKING_CHANNEL_NAME = "run"
    const val TRACKING_NOTIFICATION_ID = 1

    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val ACTION_START_TRACKING_SERVICE = "ACTION_START_SERVICE"
    const val ACTION_RESUME_TRACKING_SERVICE = "ACTION_RESUME_SERVICE"
    const val ACTION_PAUSE_TRACKING_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_TRACKING_SERVICE = "ACTION_STOP_SERVICE"

    const val TIMER_INTERVAL = 1000L

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_WIDTH = 15f
    const val MAP_ZOOM = 15f

    const val DATA_STORE_NAME = "settings.preferences_pb"
    const val DATA_STORE_THEME_KEY = "app_theme"
}