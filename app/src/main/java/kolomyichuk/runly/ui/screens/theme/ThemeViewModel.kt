package kolomyichuk.runly.ui.screens.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.local.datastore.model.AppTheme
import kolomyichuk.runly.data.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repository: SettingsRepositoryImpl
) : ViewModel() {

    val themeState = repository.themeState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppTheme.SYSTEM
    )

    fun saveTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.saveTheme(theme)
        }
    }
}