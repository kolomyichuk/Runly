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
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.RunState
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunStateUseCase
import kolomyichuk.runly.domain.run.usecase.UpdateRunStateUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.NotificationHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var getRunStateUseCase: GetRunStateUseCase

    @Inject
    lateinit var getRunDisplayModelUseCase: GetRunDisplayModelUseCase

    @Inject
    lateinit var updateRunStateUseCase: UpdateRunStateUseCase

    private var lastValidLocation: LatLng? = null
    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null
    private var notificationJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(
            TRACKING_CHANNEL_ID,
            TRACKING_CHANNEL_NAME
        )
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
        updateRunStateUseCase.invoke {
            copy(isTracking = true, isActiveRun = true, isPause = false)
        }
        timeRun = 0
        startTime = System.currentTimeMillis()

        val currentTime = getRunStateUseCase().value.timeInMillis
        val notification = notificationHelper.getNotification(
            getString(R.string.run),
            FormatterUtils.formatTime(currentTime)
        )
        startForeground(TRACKING_NOTIFICATION_ID, notification)
        startTimer()
        startNotificationUpdater()
        startLocationTracking()
    }

    private fun startTimer() {
        if (timerJob != null) return

        timerJob = serviceScope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e("Timer error: ${throwable.localizedMessage}")
        }) {
            while (true) {
                delay(TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                val updatedTime = timeRun + (currentTime - startTime)

                updateRunStateUseCase { copy(timeInMillis = updatedTime) }
            }
        }
    }

    private fun startNotificationUpdater() {
        if (notificationJob != null) return

        notificationJob = serviceScope.launch {
            getRunDisplayModelUseCase.invoke().collectLatest { run ->
                if (run.isTracking) {
                    val unitDistance = when (run.unit) {
                        DistanceUnit.KILOMETERS -> getString(R.string.km)
                        DistanceUnit.MILES -> getString(R.string.miles)
                    }
                    val text = "${run.duration} · ${run.distance} $unitDistance"
                    notificationHelper.updateNotification(
                        TRACKING_NOTIFICATION_ID,
                        getString(R.string.run),
                        text
                    )
                } else if (run.isPause) {
                    notificationHelper.updateNotification(
                        TRACKING_NOTIFICATION_ID,
                        getString(R.string.paused),
                        run.duration
                    )
                }
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
                    val currentPath = getRunStateUseCase().value.pathPoints.map { path ->
                        path.map { point ->
                            LatLng(point.latitude, point.longitude)
                        }
                    }
                    val segmentDistance = currentPath.map { segment ->
                        SphericalUtil.computeLength(segment)
                    }
                    val totalDistance = segmentDistance.sum()
                    updateRunStateUseCase { copy(distanceInMeters = totalDistance) }
                    lastValidLocation = newPoint
                }
            }
        }
    }

    private fun addLocationPoints(point: LatLng) {
        val updatedPath = getRunStateUseCase().value.pathPoints.map { path ->
            path.map {
                LatLng(it.latitude, it.longitude)
            }
        }.toMutableList()
        if (updatedPath.isEmpty()) {
            updatedPath.add(listOf(point))
        } else {
            val lastSegment = updatedPath.last().toMutableList()
            lastSegment.add(point)
            updatedPath[updatedPath.lastIndex] = lastSegment
        }
        updateRunStateUseCase {
            copy(pathPoints = updatedPath.map { path ->
                path.map {
                    RoutePoint(it.latitude, it.longitude)
                }
            }.toList())
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
        updateRunStateUseCase { copy(isTracking = false, isPause = true) }
        timeRun += System.currentTimeMillis() - startTime
        stopTimer()
        stopLocationTracking()
    }

    private fun resumeTracking() {
        if (!getRunStateUseCase().value.isTracking) {
            updateRunStateUseCase {
                val updatedPath = getRunStateUseCase().value.pathPoints.toMutableList()
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
        updateRunStateUseCase { RunState() }
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

    private fun stopNotificationUpdater() {
        notificationJob?.cancel()
        notificationJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationTracking()
        stopTimer()
        stopNotificationUpdater()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

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
}

