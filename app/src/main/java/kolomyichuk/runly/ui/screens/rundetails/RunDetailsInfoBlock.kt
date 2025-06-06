package kolomyichuk.runly.ui.screens.rundetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.model.DistanceUnit
import kolomyichuk.runly.ui.components.MetricItem

@Composable
fun RunDetailsInfoBlock(
    distance: String,
    duration: String,
    avgSpeed: String,
    unit: DistanceUnit
) {
    val unitLabel = when (unit) {
        DistanceUnit.KILOMETERS -> stringResource(R.string.km)
        DistanceUnit.MILES -> stringResource(R.string.miles)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricItem(distance, unitLabel)
            MetricItem(avgSpeed, stringResource(R.string.avg_speed))
            MetricItem(duration, stringResource(R.string.time))
        }
    }
}