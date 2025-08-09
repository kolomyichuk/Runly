package kolomyichuk.runly.ui.screens.run

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.domain.run.model.RoutePoint
import kolomyichuk.runly.domain.run.model.Run
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.run.usecase.GetRunStateUseCase
import kolomyichuk.runly.domain.run.usecase.InsertRunInFirestoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val getRunStateUseCase: GetRunStateUseCase,
    private val insertRunInFirestoreUseCase: InsertRunInFirestoreUseCase,
    getRunDisplayModelUseCase: GetRunDisplayModelUseCase
) : ViewModel() {

    val runDisplayState = getRunDisplayModelUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RunDisplayModel()
    )

    fun saveRun() {
        viewModelScope.launch(Dispatchers.Default) {
            val state = getRunStateUseCase().value
            val routePoints = state.pathPoints.map { path ->
                path.map { latLng ->
                    RoutePoint(latLng.latitude, latLng.longitude)
                }
            }
            val run = Run(
                timestamp = System.currentTimeMillis(),
                durationInMillis = state.timeInMillis,
                distanceInMeters = state.distanceInMeters,
                routePoints = routePoints
            )
            insertRunInFirestoreUseCase(run)
        }
    }
}