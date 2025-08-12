package kolomyichuk.runly.ui.screens.unitsofmeasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnitsOfMeasureViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val distanceUnitDataState = settingsRepository.distanceUnitDataState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DistanceUnit.KILOMETERS
    )

    fun saveDistanceUnit(distanceUnit: DistanceUnit) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveDistanceUnit(distanceUnit)
        }
    }
}