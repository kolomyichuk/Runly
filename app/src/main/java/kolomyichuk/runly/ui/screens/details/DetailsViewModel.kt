package kolomyichuk.runly.ui.screens.details

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
class DetailsViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val _run = MutableStateFlow<Run?>(null)
    val run: StateFlow<Run?> = _run.asStateFlow()

    fun loadRun(runId: Int) {
        viewModelScope.launch {
                val result = runRepository.getRunById(runId)
                _run.value = result
        }
    }
}