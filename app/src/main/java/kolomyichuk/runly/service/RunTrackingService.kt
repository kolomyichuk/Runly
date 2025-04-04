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
import kotlinx.coroutines.flow.update
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
        val pathPoints = MutableStateFlow<MutableList<MutableList<LatLng>>>(mutableListOf())
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
                Constants.ACTION_START_TRACKING_SERVICE -> startTracking()
                Constants.ACTION_PAUSE_TRACKING_SERVICE -> pauseTracking()
                Constants.ACTION_RESUME_TRACKING_SERVICE -> resumeTracking()
                Constants.ACTION_STOP_TRACKING_SERVICE -> stopTracking()
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
        Timber.d("Add location points call")
        pathPoints.update { list ->
            if (list.isEmpty()) {
                list.add(mutableListOf())
            }
            list.last().add(point)
            list
        }
        Timber.d("Path Points now: ${pathPoints.value}")
    }

    private fun pauseTracking() {
        isTracking.value = false
        isPause.value = true
        Timber.d("Paused")
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
            Timber.d("Resume")
            pathPoints.update { list->
                list.add(mutableListOf())
                list
            }
            Timber.d("Path: ${pathPoints.value}")
            startLocationTracking()
        }
    }

    private fun stopTracking() {
        isTracking.value = false
        isPause.value = false
        timeRun = 0
        Timber.d("Stop Tracking")
        isActiveRun.value = false
        distanceInMeters.value = 0.0
        timeInMillis.value = 0L
        pathPoints.update { mutableListOf() }
        Timber.d("Path stop ${pathPoints.value}")
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