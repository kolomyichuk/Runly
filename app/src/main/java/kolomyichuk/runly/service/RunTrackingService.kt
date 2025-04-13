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
import kolomyichuk.runly.utils.Constants
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackingService : Service() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var lastValidLocation: LatLng? = null
    private var startTime = 0L
    private var timeRun = 0L
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        val timeInMillis = MutableStateFlow<Long>(0)
        val isTracking = MutableStateFlow(false)
        val isActiveRun = MutableStateFlow(false)
        val isPause = MutableStateFlow(false)
        val pathPoints = MutableStateFlow<List<List<LatLng>>>(mutableListOf())
        val distanceInMeters = MutableStateFlow(0.0)
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel(
            Constants.TRACKING_CHANNEL_ID,
            Constants.TRACKING_CHANNEL_NAME
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when (it?.action) {
                Constants.ACTION_START_TRACKING -> startTracking()
                Constants.ACTION_PAUSE_TRACKING -> pauseTracking()
                Constants.ACTION_RESUME_TRACKING -> resumeTracking()
                Constants.ACTION_STOP_TRACKING -> stopTracking()
            }
        }
        return START_STICKY
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

        startForeground(Constants.TRACKING_NOTIFICATION_ID, notification)
        startTimer()
        startLocationTracking()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            Constants.LOCATION_UPDATE_INTERVAL
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
                Timber.d("newPoint ${newPoint.latitude}, ${newPoint.longitude}")
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
            Constants.TRACKING_NOTIFICATION_ID,
            getString(R.string.paused),
            TrackingUtility.formatTime(timeInMillis.value)
        )
    }

    private fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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

    private fun stopTracking() {
        isTracking.value = false
        isPause.value = false
        timeRun = 0
        isActiveRun.value = false
        distanceInMeters.value = 0.0
        timeInMillis.value = 0L
        pathPoints.value = emptyList()
        stopTimer()
        stopLocationTracking()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startTimer() {
        if (timerJob != null) return

        timerJob = serviceScope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e("Timer error: ${throwable.localizedMessage}")
        }) {
            while (isTracking.value) {
                delay(Constants.TIMER_INTERVAL)
                val currentTime = System.currentTimeMillis()
                timeInMillis.value = timeRun + (currentTime - startTime)

                notificationHelper.updateNotification(
                    Constants.TRACKING_NOTIFICATION_ID,
                    getString(R.string.run),
                    TrackingUtility.formatTime(timeInMillis.value)
                )
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