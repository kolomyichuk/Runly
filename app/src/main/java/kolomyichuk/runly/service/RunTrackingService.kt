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
import kolomyichuk.runly.data.model.RunState
import kolomyichuk.runly.data.repository.RunRepository
import kolomyichuk.runly.utils.NotificationHelper
import kolomyichuk.runly.utils.TrackingUtility
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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

    @Inject
    lateinit var runRepository: RunRepository

    companion object {
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

    private fun startTracking() {
        runRepository.updateRunState {
            copy(isTracking = true, isActiveRun = true, isPause = false)
        }
        timeRun = 0
        startTime = System.currentTimeMillis()

        val notification = notificationHelper.getNotification(
            getString(R.string.run),
            TrackingUtility.formatTime(runRepository.runState.value.timeInMillis)
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
            while (runRepository.runState.value.isTracking) {
                delay(TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                val updatedTime = timeRun + (currentTime - startTime)

                runRepository.updateRunState { copy(timeInMillis = updatedTime) }

                val formattedDistance =
                    TrackingUtility.formatDistanceToKm(runRepository.runState.value.distanceInMeters)

                notificationHelper.updateNotification(
                    TRACKING_NOTIFICATION_ID,
                    getString(R.string.run),
                    "${TrackingUtility.formatTime(updatedTime)} · $formattedDistance km"
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
                    val currentPath = runRepository.runState.value.pathPoints
                    val segmentDistance = currentPath.map { segment ->
                        SphericalUtil.computeLength(segment)
                    }
                    val totalDistance = segmentDistance.sum()
                    runRepository.updateRunState { copy(distanceInMeters = totalDistance) }
                    lastValidLocation = newPoint
                }
            }
        }
    }

    private fun addLocationPoints(point: LatLng) {
        val updatedPath = runRepository.runState.value.pathPoints.toMutableList()
        if (updatedPath.isEmpty()) {
            updatedPath.add(listOf(point))
        } else {
            val lastSegment = updatedPath.last().toMutableList()
            lastSegment.add(point)
            updatedPath[updatedPath.lastIndex] = lastSegment
        }
        runRepository.updateRunState { copy(pathPoints = updatedPath.toList()) }
    }

    private fun speedCalculation() {
        serviceScope.launch {
            runRepository.runState
                .map { state ->
                    val distance = state.distanceInMeters
                    val time = state.timeInMillis
                    val tracking = state.isTracking

                    if (!tracking) return@map null

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
                    runRepository.updateRunState { copy(avgSpeed = calculatedSpeed) }
                }
        }
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

    private fun pauseTracking() {
        runRepository.updateRunState { copy(isTracking = false, isPause = true) }
        timeRun += System.currentTimeMillis() - startTime
        stopTimer()
        stopLocationTracking()
        val formattedTime = TrackingUtility.formatTime(runRepository.runState.value.timeInMillis)
        notificationHelper.updateNotification(
            TRACKING_NOTIFICATION_ID,
            getString(R.string.paused),
            formattedTime
        )
    }

    private fun resumeTracking() {
        if (!runRepository.runState.value.isTracking) {
            runRepository.updateRunState {
                val updatedPath = runRepository.runState.value.pathPoints.toMutableList()
                updatedPath.add(emptyList())
                copy(isTracking = true, isPause = false, pathPoints = updatedPath.toList())
            }
            startTime = System.currentTimeMillis()
            startTimer()
            startLocationTracking()
        }
    }

    private fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun stopTracking() {
        runRepository.updateRunState { RunState() }
        timeRun = 0
        stopTimer()
        stopLocationTracking()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
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

