package kolomyichuk.runly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    fun saveRun(run: Run) {
        viewModelScope.launch {
            runRepository.insertRun(run)
        }
    }
}