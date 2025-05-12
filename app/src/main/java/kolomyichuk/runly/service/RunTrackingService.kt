package kolomyichuk.runly.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.R
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        // TODO We should avoid MutableStateFlows that available as singletons
        // TODO This approach is very error-prone
        // TODO Let's inject RunRepository here and communicate through it
        // TODO The communication should be - Service -> Repository <- ViewModel <- View
        // TODO Service updates a State of the Repository
        // TODO ViewModel listens to the Repository state and then View listen to the ViewModel
        val timeInMillis = MutableStateFlow<Long>(0)
        val isTracking = MutableStateFlow(false)
        val isActiveRun = MutableStateFlow(false)
        val isPause = MutableStateFlow(false)
        val pathPoints = MutableStateFlow<List<List<LatLng>>>(mutableListOf())
        val distanceInMeters = MutableStateFlow(0.00)
        var avgSpeed = MutableStateFlow(0.00f)

        const val ACTION_START_TRACKING = "ACTION_START_SERVICE"
        const val ACTION_RESUME_TRACKING = "ACTION_RESUME_SERVICE"
        const val ACTION_PAUSE_TRACKING = "ACTION_PAUSE_SERVICE"
        const val ACTION_STOP_TRACKING = "ACTION_STOP_SERVICE"

        const val TRACKING_CHANNEL_ID = "ch-1"
        private const val TRACKING_CHANNEL_NAME = "run"
        private const val TRACKING_NOTIFICATION_ID = 1

        private const val LOCATION_UPDATE_INTERVAL = 5000L
        private const val TIMER_INTERVAL = 1000L
    }

    private var lastValidLocation: LatLng? = null
    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(
            TRACKING_CHANNEL_ID,
            TRACKING_CHANNEL_NAME
        )
        speedCalculation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when (it?.action) {
                ACTION_START_TRACKING -> startTracking()
                ACTION_PAUSE_TRACKING -> pauseTracking()
                ACTION_RESUME_TRACKING -> resumeTracking()
                ACTION_STOP_TRACKING -> stopTracking()
            }
        }
        return START_STICKY
    }

    private fun speedCalculation() {
        serviceScope.launch {
            combine(distanceInMeters, timeInMillis, isTracking) { distance, time, tracking ->
                if (!tracking) return@combine null

                val timeInSeconds = time / 1000.0
                val distanceInKm = distance / 1000.0

                if (timeInSeconds > 5 && distanceInKm > 0.02) {
                    val speed = distanceInKm / (timeInSeconds / 3600.0)
                    if (speed.isFinite()) {
                        String.format(Locale.US, "%.2f", speed).toFloat()
                    } else null
                } else null
            }
                .filterNotNull()
                .catch { e -> Timber.e("Error in speedCalculation: ${e.localizedMessage}") }
                .collect { calculatedSpeed ->
                    avgSpeed.value = calculatedSpeed
                }
        }
    }

    private fun startTracking() {
        isTracking.value = true
        isActiveRun.value = true
        isPause.value = false
        timeRun = 0
        timeInMillis.value = 0L
        startTime = System.currentTimeMillis()

        val notification = notificationHelper.getNotification(
            getString(R.string.run),
            TrackingUtility.formatTime(timeInMillis.value)
        )

        startForeground(TRACKING_NOTIFICATION_ID, notification)
        startTimer()
        startLocationTracking()
    }

    private fun startTimer() {
        if (timerJob != null) return

        timerJob = serviceScope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e("Timer error: ${throwable.localizedMessage}")
        }) {
            while (isTracking.value) {
                delay(TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                timeInMillis.value = timeRun + (currentTime - startTime)

                val formattedDistance = TrackingUtility.formatDistanceToKm(distanceInMeters.value)

                notificationHelper.updateNotification(
                    TRACKING_NOTIFICATION_ID,
                    getString(R.string.run),
                    "${TrackingUtility.formatTime(timeInMillis.value)} · $formattedDistance km"
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )
            .setMinUpdateDistanceMeters(1f)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.lastOrNull()?.let { location ->
                val newPoint = LatLng(location.latitude, location.longitude)
                val timeDiff = (location.time - (lastValidLocation?.let {
                    Location("").apply {
                        latitude = it.latitude
                        longitude = it.longitude
                    }
                }?.time ?: location.time)) / 1000

                if (lastValidLocation == null || isValidLocation(
                        lastValidLocation!!,
                        newPoint,
                        timeDiff.toFloat()
                    )
                ) {
                    addLocationPoints(newPoint)
                    val segmentDistance = pathPoints.value.map { segment ->
                        SphericalUtil.computeLength(segment)
                    }
                    distanceInMeters.value = segmentDistance.sum()
                    lastValidLocation = newPoint
                }
            }
        }
    }

    private fun addLocationPoints(point: LatLng) {
        val updatedPath = pathPoints.value.toMutableList()
        if (updatedPath.isEmpty()) {
            updatedPath.add(listOf(point))
        } else {
            val lastSegment = updatedPath.last().toMutableList()
            lastSegment.add(point)
            updatedPath[updatedPath.lastIndex] = lastSegment
        }
        pathPoints.value = updatedPath.toList()
    }

    private fun pauseTracking() {
        isTracking.value = false
        isPause.value = true
        timeRun += System.currentTimeMillis() - startTime
        stopTimer()
        stopLocationTracking()
        notificationHelper.updateNotification(
            TRACKING_NOTIFICATION_ID,
            getString(R.string.paused),
            TrackingUtility.formatTime(timeInMillis.value)
        )
    }

    private fun resumeTracking() {
        if (!isTracking.value) {
            isTracking.value = true
            isPause.value = false
            startTime = System.currentTimeMillis()
            startTimer()
            val updated = pathPoints.value.toMutableList()
            updated.add(emptyList())
            pathPoints.value = updated.toList()
            startLocationTracking()
        }
    }

    private fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun stopTracking() {
        isTracking.value = false
        isPause.value = false
        timeRun = 0
        avgSpeed.value = 0.00f
        isActiveRun.value = false
        distanceInMeters.value = 0.0
        timeInMillis.value = 0L
        pathPoints.value = emptyList()
        stopTimer()
        stopLocationTracking()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun isValidLocation(prevPoint: LatLng, newPoint: LatLng, timeDiff: Float): Boolean {
        if (timeDiff <= 0f) return false

        val distance = FloatArray(1)
        Location.distanceBetween(
            prevPoint.latitude, prevPoint.longitude,
            newPoint.latitude, newPoint.longitude, distance
        )
        val maxSpeed = 30 / 3.6
        return distance[0] < 50 && distance[0] / timeDiff < maxSpeed
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationTracking()
        stopTimer()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}

