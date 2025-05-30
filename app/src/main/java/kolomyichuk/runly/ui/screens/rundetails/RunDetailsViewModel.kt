package kolomyichuk.runly.ui.screens.rundetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.RunRepository
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunDetailsViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val _runDetailsState = MutableStateFlow(RunDetailsState())
    val runDetailsState: StateFlow<RunDetailsState> = _runDetailsState.asStateFlow()

    fun loadRun(runId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val run = runRepository.getRunById(runId)

            _runDetailsState.value = RunDetailsState(
                dateTime = run.timestamp.toFormattedDateTime(),
                distance = FormatterUtils.formatDistanceToKm(run.distanceInMeters),
                avgSpeed = run.avgSpeed.toString(),
                duration = FormatterUtils.formatTime(run.durationInMillis),
                pathPoints = run.routePoints.map { path ->
                    path.map { LatLng(it.latitude, it.longitude) }
                }
            )
        }
    }
}