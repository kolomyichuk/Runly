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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.ui.components.MetricItem
import kolomyichuk.runly.ui.ext.getUnitLabel

@Composable
fun HomeRunItem(
    run: RunDisplayModel,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val unitLabel = stringResource(run.unit.getUnitLabel())

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.date),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = run.dateTime,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                HomeRunDelete(
                    onDelete = { onDelete() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(run.distance, unitLabel)
                MetricItem(run.avgSpeed, stringResource(R.string.avg_speed))
                MetricItem(run.duration, stringResource(R.string.time))
            }
        }
    }
}

