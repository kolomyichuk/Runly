package kolomyichuk.runly.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.model.DistanceUnitData
import kolomyichuk.runly.domain.settings.model.DistanceUnit
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.ext.getUnitLabel

@Composable
fun DashboardInfoBlock(
    distance: String,
    time: String,
    avgSpeed: String,
    distanceUnit: DistanceUnit
) {
    val unitLabel = stringResource(distanceUnit.getUnitLabel())
    val speedUnit = when (distanceUnit) {
        DistanceUnit.KILOMETERS -> stringResource(R.string.km_h)
        DistanceUnit.MILES -> stringResource(R.string.miles_per_hour)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardInfoColumn(
            title = stringResource(R.string.time),
            value = time,
            type = " "
        )
        HorizontalLineDivider()
        DashboardInfoColumn(
            title = stringResource(R.string.avg_speed),
            value = avgSpeed,
            type = speedUnit
        )
        HorizontalLineDivider()
        DashboardInfoColumn(
            title = stringResource(R.string.distance),
            value = distance,
            type = unitLabel
        )
    }
}

@Composable
private fun DashboardInfoColumn(
    title: String,
    value: String,
    type: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = value,
            fontSize = 50.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = type,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}