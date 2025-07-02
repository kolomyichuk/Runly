package kolomyichuk.runly.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signOutSuccess = MutableStateFlow(false)
    val signOutSuccess: StateFlow<Boolean> = _signOutSuccess

    private val _signOutError = MutableStateFlow<String?>(null)
    val signOutError: StateFlow<String?> = _signOutError

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signOut()
            if (result.isSuccess) {
                _signOutSuccess.value = true
            } else {
                _signOutError.value = result.exceptionOrNull()?.message ?: "Sign out failed"
            }
        }
    }

    fun resetStates() {
        _signOutSuccess.value = false
        _signOutError.value = null
    }
}