package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    val runs: StateFlow<List<RunDisplayModel>> = runRepository.getAllRuns()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun deleteRun(run: Run) {
        viewModelScope.launch {
            runRepository.deleteRun(run)
        }
    }
}