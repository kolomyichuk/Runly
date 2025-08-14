package kolomyichuk.runly.ui.screens.unitsofmeasure

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.ext.getUnitLabel

@Composable
fun UnitsOfMeasureScreen(
    onBack: () -> Unit,
    unitsOfMeasureViewModel: UnitsOfMeasureViewModel
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.units_of_measure),
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        UnitsOfMeasureScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            unitsOfMeasureViewModel = unitsOfMeasureViewModel
        )
    }
}


@Composable
private fun UnitsOfMeasureScreenContent(
    modifier: Modifier = Modifier,
    unitsOfMeasureViewModel: UnitsOfMeasureViewModel
) {
    val selectedUnit by unitsOfMeasureViewModel.distanceUnitDataState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        DistanceUnit.entries.forEachIndexed { index, unit ->
            val label = stringResource(unit.getUnitLabel())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { unitsOfMeasureViewModel.saveDistanceUnit(unit) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                RadioButton(
                    selected = selectedUnit == unit,
                    onClick = { unitsOfMeasureViewModel.saveDistanceUnit(unit) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }

            if (index < DistanceUnit.entries.lastIndex) {
                HorizontalLineDivider()
            }
        }
    }
}