package kolomyichuk.runly.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _imageFilePath = MutableStateFlow<String?>(null)
    val imageFilePath = _imageFilePath.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUsername().collect { name ->
                _username.value = name
            }
        }
        loadProfileImage()
    }

    fun saveUsername(newUsername: String) {
        viewModelScope.launch {
            repository.saveUsername(newUsername)
        }
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            val newFilePath = repository.saveProfileImage(uri)
            _imageFilePath.value = newFilePath
        }
    }

    private fun loadProfileImage() {
        viewModelScope.launch {
            _imageFilePath.value = repository.loadProfileImage()
        }
    }
}

