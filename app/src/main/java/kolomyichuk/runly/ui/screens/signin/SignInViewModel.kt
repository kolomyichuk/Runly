package kolomyichuk.runly.ui.screens.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun signInWithGoogle(idToken: String) = withContext(Dispatchers.IO) {
        authRepository.signInWithGoogle(idToken)
    }
}
