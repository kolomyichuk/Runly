package kolomyichuk.runly.ui.screens.run

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.LatLngPoint
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    val runState = runRepository.runState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = runRepository.runState.value
    )

    fun saveRun() {
        viewModelScope.launch {
            val state = runState.value
            val routePoints = state.pathPoints.map { path ->
                path.map { latLng ->
                    LatLngPoint(latLng.latitude, latLng.longitude)
                }
            }
            val run = Run(
                timestamp = Calendar.getInstance().timeInMillis,
                durationInMillis = state.timeInMillis,
                distanceInMeters = state.distanceInMeters,
                avgSpeed = state.avgSpeed,
                routePoints = routePoints
            )
            runRepository.insertRun(run)
        }
    }
}