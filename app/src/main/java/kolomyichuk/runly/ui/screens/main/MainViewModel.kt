package kolomyichuk.runly.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.model.AppTheme
import kolomyichuk.runly.data.repository.AuthRepository
import kolomyichuk.runly.data.repository.RunRepository
import kolomyichuk.runly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    runRepository: RunRepository,
    themeRepository: SettingsRepository,
) : ViewModel() {

    val isUserSignIn = authRepository.isUserSignedIn().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val runState = runRepository.runState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = runRepository.runState.value
    )

    val themeState = themeRepository.themeState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppTheme.SYSTEM
    )
}