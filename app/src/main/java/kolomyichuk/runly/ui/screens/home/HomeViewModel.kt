package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val _runs = MutableStateFlow<List<Run>>(emptyList())
    val runs: StateFlow<List<Run>> = _runs.asStateFlow()

    private val _snackBarMessages = MutableSharedFlow<HomeSnackBarData>()
    val snackBarMessages = _snackBarMessages.asSharedFlow()

    private var recentlyDeletedRun: Run? = null

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

    fun deleteRun(run: Run, message: String, actionLabel: String) {
        viewModelScope.launch {
            recentlyDeletedRun = run
            runRepository.deleteRun(run)
            _snackBarMessages.emit(
                HomeSnackBarData(message = message, actionLabel = actionLabel)
            )
        }
    }

    fun undoDelete() {
        recentlyDeletedRun?.let { run ->
            viewModelScope.launch {
                runRepository.insertRun(run)
                recentlyDeletedRun = null
            }
        }
    }
}