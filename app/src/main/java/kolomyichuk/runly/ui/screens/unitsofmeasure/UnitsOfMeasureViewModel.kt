package kolomyichuk.runly.ui.screens.unitsofmeasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.data.model.DistanceUnit
import kolomyichuk.runly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnitsOfMeasureViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val distanceUnitState = settingsRepository.distanceUnitState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DistanceUnit.KILOMETERS
    )

    fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        viewModelScope.launch {
            settingsRepository.saveDistanceUnit(distanceUnit)
        }
    }
}