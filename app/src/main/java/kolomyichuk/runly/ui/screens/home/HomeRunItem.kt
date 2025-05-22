package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.room.entity.Run
import kolomyichuk.runly.utils.TrackingUtility

@Composable
fun HomeRunItem(
    run: Run
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = run.timestamp.toString(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = stringResource(R.string.date))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val distance = TrackingUtility.formatDistanceToKm(run.distanceInMeters)
                val avgSpeed = run.avgSpeed.toString()
                val time = TrackingUtility.formatTime(run.durationInMillis)

                HomeMetricItem(distance, stringResource(R.string.km))
                HomeMetricItem(avgSpeed, stringResource(R.string.avg_speed))
                HomeMetricItem(time, stringResource(R.string.time))
            }
        }
    }
}

