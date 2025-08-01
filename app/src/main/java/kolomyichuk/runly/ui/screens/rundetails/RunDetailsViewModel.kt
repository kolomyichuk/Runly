package kolomyichuk.runly.ui.screens.rundetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.repository.RunRepository
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

    private val _runDetailsState = MutableStateFlow(RunDisplayModel())
    val runDetailsState: StateFlow<RunDisplayModel> = _runDetailsState.asStateFlow()

    fun loadRun(runId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            runRepository.getRunByIdFromFirestore(runId).collect { run ->
                _runDetailsState.value = run
            }
        }
    }
}