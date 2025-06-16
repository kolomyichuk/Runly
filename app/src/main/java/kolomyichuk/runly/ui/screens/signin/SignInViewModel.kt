package kolomyichuk.runly.ui.screens.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun signInWithGoogle(idToken: String) =
        authRepository.signInWithGoogle(idToken)
}