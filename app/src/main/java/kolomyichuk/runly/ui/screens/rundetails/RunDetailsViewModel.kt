package kolomyichuk.runly.ui.screens.rundetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.usecase.GetRunByIdFromFirestoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunDetailsViewModel @Inject constructor(
    private val getRunByIdFromFirestoreUseCase: GetRunByIdFromFirestoreUseCase
) : ViewModel() {

    private val _runDetailsState = MutableStateFlow(RunDisplayModel())
    val runDetailsState: StateFlow<RunDisplayModel> = _runDetailsState.asStateFlow()

    fun loadRun(runId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            getRunByIdFromFirestoreUseCase(runId).collect { run ->
                _runDetailsState.value = run
            }
        }
    }
}
