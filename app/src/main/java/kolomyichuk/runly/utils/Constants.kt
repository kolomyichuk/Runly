package kolomyichuk.runly.utils

// TODO Let's avoid God objects with constants. Please, move them to the appropriate class when they are used
//
object Constants {
    // TODO Move them to the RunTrackingService class
    const val TRACKING_CHANNEL_ID = "ch-1"
    const val TRACKING_CHANNEL_NAME = "run"
    const val TRACKING_NOTIFICATION_ID = 1

    // TODO Move it to the RunTrackingService class
    const val ACTION_SHOW_RUN_SCREEN = "ACTION_SHOW_RUN_SCREEN"

    // TODO Move them to the RunTrackingService class and make them public to use in other classes
    const val ACTION_START_TRACKING = "ACTION_START_SERVICE"
    const val ACTION_RESUME_TRACKING = "ACTION_RESUME_SERVICE"
    const val ACTION_PAUSE_TRACKING = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_TRACKING = "ACTION_STOP_SERVICE"

    // TODO Move it to the RunTrackingService class
    const val TIMER_INTERVAL = 1000L
    const val LOCATION_UPDATE_INTERVAL = 5000L

    // TODO Move it to the RunMapView class
    const val POLYLINE_WIDTH = 12f
    const val MAP_ZOOM = 15f

    // TODO Move it to the DatabaseModule class
    const val PROFILE_DATA_STORE_NAME = "profile.preferences_pb"
    const val THEME_DATA_STORE_NAME = "theme.preferences_pb"
}