package kolomyichuk.runly.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val username = repository.getUsername().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        "username"
    )

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile = _imageFile.asStateFlow()

    init {
        loadProfileImage()
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            val newFile = repository.saveProfileImage(uri)
            _imageFile.value = newFile
        }
    }

    private fun loadProfileImage() {
        viewModelScope.launch {
            _imageFile.value = repository.loadProfileImage()
        }
    }
}

