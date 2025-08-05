package kolomyichuk.runly.ui.screens.home

import kolomyichuk.runly.data.model.RunDisplayModel

sealed class RunUiState {
    data object Loading : RunUiState()
    data class Success(val runs: List<RunDisplayModel>) : RunUiState()
    data class Error(val type: ErrorType) : RunUiState()
}

