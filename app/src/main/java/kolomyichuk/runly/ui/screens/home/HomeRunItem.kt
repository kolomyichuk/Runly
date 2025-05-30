package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.ui.components.MetricItem
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime

@Composable
fun HomeRunItem(
    run: Run,
    onClick: () -> Unit
) {
    val formattedDate = remember(run.timestamp) {
        run.timestamp.toFormattedDateTime()
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable { onClick() }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(
                    text = stringResource(R.string.date),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formattedDate,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val distance by remember(run.distanceInMeters) {
                    derivedStateOf { FormatterUtils.formatDistanceToKm(run.distanceInMeters) }
                }
                val avgSpeed by remember(run.avgSpeed) {
                    derivedStateOf { run.avgSpeed.toString() }
                }
                val time by remember(run.durationInMillis) {
                    derivedStateOf { FormatterUtils.formatTime(run.durationInMillis) }
                }

                MetricItem(distance, stringResource(R.string.km))
                MetricItem(avgSpeed, stringResource(R.string.avg_speed))
                MetricItem(time, stringResource(R.string.time))
            }
        }
    }
}

