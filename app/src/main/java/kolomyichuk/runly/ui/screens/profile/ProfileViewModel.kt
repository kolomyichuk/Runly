package kolomyichuk.runly.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.repository.ProfileRepository
import kolomyichuk.runly.domain.run.usecase.GetThisWeekDistanceByDayUseCase
import kolomyichuk.runly.domain.run.usecase.GetTotalDistanceUseCase
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val runRepository: ProfileRepository,
    private val getThisWeekDistanceByDayUseCase: GetThisWeekDistanceByDayUseCase,
    private val getTotalDistanceUseCase: GetTotalDistanceUseCase,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _thisWeekDistanceByDay = MutableStateFlow(List(size = 7) { 0f })
    val thisWeekDistanceByDay: StateFlow<List<Float>> = _thisWeekDistanceByDay.asStateFlow()

    private val _totalDistance = MutableStateFlow(0f)
    val totalDistance: StateFlow<Float> = _totalDistance

    val distanceUnit: StateFlow<DistanceUnit> = settingsRepository.getDistanceUnit()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DistanceUnit.KILOMETERS
        )

    init {
        loadUserProfile()
        loadThisWeekDistanceByDay()
        loadTotalDistance()
    }

    private fun loadTotalDistance() {
        viewModelScope.launch {
            val data = getTotalDistanceUseCase.invoke()
            _totalDistance.value = data
        }
    }

    private fun loadThisWeekDistanceByDay() {
        viewModelScope.launch {
            val data = getThisWeekDistanceByDayUseCase.invoke()
            _thisWeekDistanceByDay.value = data
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val name = runRepository.getCurrentUserName()
            val photo = runRepository.getCurrentPhotoUrl()
            _userProfile.update {
                it.copy(
                    name = name,
                    photoUrl = photo
                )
            }
        }
    }
}
