package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.Dispatchers
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

    private val _homeEffects = MutableSharedFlow<HomeEffect>()
    val homeEffects = _homeEffects.asSharedFlow()

    private var recentlyDeleteRun: Run? = null

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

    fun confirmDeleteRun() {
        recentlyDeleteRun?.let { run ->
            viewModelScope.launch(Dispatchers.IO) {
                runRepository.deleteRun(run)
            }
        }
    }

    fun requestDeleteRun(run: Run) {
        viewModelScope.launch {
            recentlyDeleteRun = run
            _homeEffects.emit(HomeEffect.ShowDeleteSnackBar)
        }
    }

    fun undoDeleteRun(){
        recentlyDeleteRun = null
    }
}