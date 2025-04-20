package kolomyichuk.runly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val _runs = MutableStateFlow<List<Run>>(emptyList())
    val runs: StateFlow<List<Run>> = _runs.asStateFlow()

    init {
        getAllRuns()
    }

    private fun getAllRuns() {
        viewModelScope.launch {
            runRepository.getAllRuns()
                .collect { runList ->
                    _runs.value = runList
                }
        }
    }

    fun deleteRun(run: Run) {
        viewModelScope.launch {
            runRepository.deleteRun(run)
        }
    }
}