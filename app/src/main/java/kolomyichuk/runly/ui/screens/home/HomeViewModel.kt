package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _homeEffects = MutableSharedFlow<HomeEffect>()
    val homeEffects = _homeEffects.asSharedFlow()

    private var recentlyDeleteRun: RunDisplayModel? = null

    fun confirmDeleteRun() {
        recentlyDeleteRun?.let { run ->
            viewModelScope.launch(Dispatchers.IO) {
                runRepository.deleteRunById(run.id)
            }
        }
    }

    fun requestDeleteRun(run: RunDisplayModel) {
        viewModelScope.launch {
            recentlyDeleteRun = run
            _homeEffects.emit(HomeEffect.ShowDeleteSnackBar)
        }
    }

    fun undoDeleteRun(){
        recentlyDeleteRun = null
    }
}