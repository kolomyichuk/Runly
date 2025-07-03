package kolomyichuk.runly.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signOutEffect = MutableSharedFlow<SignOutEffect>()
    val signOutEffect: SharedFlow<SignOutEffect> = _signOutEffect.asSharedFlow()

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signOut()
            if (result.isSuccess) {
                _signOutEffect.emit(SignOutEffect.Success)
            } else {
                _signOutEffect.emit(SignOutEffect.Failure)
            }
        }
    }
}