package kolomyichuk.runly.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap.asStateFlow()

    init {
        loadImage()
    }

    fun saveUsername(newUsername: String) {
        viewModelScope.launch {
            repository.saveUsername(newUsername)
        }
    }

    private fun loadImage() {
        val loadedImage = repository.loadImage()
        _bitmap.value = loadedImage
    }

    fun saveProfileImage(uri: Uri): Boolean {
        val success = repository.saveImage(uri)
        if (success) {
            loadImage()
        }
        return success
    }
}

