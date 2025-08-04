package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.model.RunDisplayModel
import kolomyichuk.runly.data.repository.RunRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RunUiState>(RunUiState.Loading)
    val uiState: StateFlow<RunUiState> = _uiState

    init {
        loadRuns()
    }

    private fun loadRuns() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository
                .getAllRunsFromFirestore()
                .catch { e ->
                    val errorType = when(e){
                        is IOException -> ErrorType.NETWORK
                        is FirebaseAuthException -> ErrorType.UNAUTHORIZED
                        else -> ErrorType.UNKNOWN
                    }
                    _uiState.value = RunUiState.Error(errorType)
                }
                .collect { runs ->
                    _uiState.value = RunUiState.Success(runs)
                }
        }
    }

    private val _homeEffects = MutableSharedFlow<HomeEffect>()
    val homeEffects = _homeEffects.asSharedFlow()

    private var recentlyDeleteRun: RunDisplayModel? = null

    fun confirmDeleteRun() {
        recentlyDeleteRun?.let { run ->
            viewModelScope.launch(Dispatchers.IO) {
                runRepository.deleteRunByIdInFirestore(run.id)
            }
        }
    }

    fun requestDeleteRun(run: RunDisplayModel) {
        viewModelScope.launch {
            recentlyDeleteRun = run
            _homeEffects.emit(HomeEffect.ShowDeleteSnackBar)
        }
    }

    fun undoDeleteRun() {
        recentlyDeleteRun = null
    }
}