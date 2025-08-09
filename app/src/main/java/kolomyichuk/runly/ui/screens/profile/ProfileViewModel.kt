package kolomyichuk.runly.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val name = repository.getCurrentUserName()
            val photo = repository.getCurrentPhotoUrl()
            _userProfile.update {
                it.copy(
                    name = name,
                    photoUrl = photo
                )
            }
        }
    }
}

