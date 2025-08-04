package kolomyichuk.runly.ui.screens.home

import kolomyichuk.runly.data.model.RunDisplayModel

sealed class RunUiState {
    object Loading : RunUiState()
    data class Success(val runs: List<RunDisplayModel>) : RunUiState()
    data class Error(val message: String) : RunUiState()
}