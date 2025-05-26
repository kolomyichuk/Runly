package kolomyichuk.runly.ui.screens.run

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.LatLngPoint
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.Default) {
            val state = runState.value
            val routePoints = state.pathPoints.map { path ->
                path.map { latLng ->
                    LatLngPoint(latLng.latitude, latLng.longitude)
                }
            }
            val run = Run(
                timestamp = System.currentTimeMillis(),
                durationInMillis = state.timeInMillis,
                distanceInMeters = state.distanceInMeters,
                avgSpeed = state.avgSpeed,
                routePoints = routePoints
            )
            runRepository.insertRun(run)
        }
    }
}