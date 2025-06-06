package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kolomyichuk.runly.data.model.DistanceUnit
import kolomyichuk.runly.ui.ext.getUnitLabel

@Composable
fun RunInfoBlock(
    distance: String,
    time: String,
    avgSpeed: String,
    distanceUnit: DistanceUnit
) {
    val unitLabel = stringResource(distanceUnit.getUnitLabel())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoColumn(distance, unitLabel)
        InfoColumn(avgSpeed, stringResource(R.string.avg_speed))
        InfoColumn(time, stringResource(R.string.time))
    }
}

@Composable
private fun InfoColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp
        )
    }
}