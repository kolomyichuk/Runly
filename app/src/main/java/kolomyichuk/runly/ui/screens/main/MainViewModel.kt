package kolomyichuk.runly.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.AuthRepository
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    getRunDisplayModelUseCase: GetRunDisplayModelUseCase,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _isUserSignIn = MutableStateFlow(false)
    val isUserSignIn: StateFlow<Boolean> = _isUserSignIn

    init {
        checkUserSignInStatus()
    }

    private fun checkUserSignInStatus() {
        _isUserSignIn.value = authRepository.isUserSignedIn()
    }

    val runState = getRunDisplayModelUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RunDisplayModel()
    )

    val themeState = settingsRepository.themeState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppTheme.SYSTEM
    )
}