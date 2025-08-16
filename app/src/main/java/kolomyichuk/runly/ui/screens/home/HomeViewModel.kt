package kolomyichuk.runly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.usecase.DeleteRunByIdInFirestoreUseCase
import kolomyichuk.runly.domain.run.usecase.GetAllRunsFromFirestoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deleteRunByIdInFirestoreUseCase: DeleteRunByIdInFirestoreUseCase,
    private val getAllRunsFromFirestoreUseCase: GetAllRunsFromFirestoreUseCase
) : ViewModel() {

    val runs: StateFlow<List<RunDisplayModel>> = getAllRunsFromFirestoreUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            emptyList()
        )
    private val _uiState = MutableStateFlow<RunUiState>(RunUiState.Loading)
    val uiState: StateFlow<RunUiState> = _uiState

    init {
        loadRuns()
    }

    private fun loadRuns() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllRunsFromFirestoreUseCase()
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
                deleteRunByIdInFirestoreUseCase(run.id)
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
