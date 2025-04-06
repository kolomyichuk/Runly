package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.ButtonStart
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.utils.pauseTrackingService
import kolomyichuk.runly.utils.resumeTrackingService
import kolomyichuk.runly.utils.startRunTrackingService
import kolomyichuk.runly.utils.stopTrackingService

@Composable
fun ControlButtonsPanel(
    isTracking: Boolean,
    isPause: Boolean
) {
    val context = LocalContext.current

    if (!isTracking && !isPause) {
        ButtonStart(
            onClick = {
                    startRunTrackingService(context = context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            text = stringResource(R.string.start),
            roundDp = 8.dp
        )
    } else {
        OtherButtons(
            isTracking = isTracking,
            isPause = isPause,
            onPause = { pauseTrackingService(context = context) },
            onResume = { resumeTrackingService(context = context) },
            onStop = { stopTrackingService(context = context) }
        )
    }
}

@Composable
fun OtherButtons(
    isTracking: Boolean,
    isPause: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        if (isTracking || isPause) {
            CircleIconButton(
                onClick = { onStop() },
                imageVector = Icons.Filled.Stop,
                iconColor = MaterialTheme.colorScheme.onPrimary,
                elevation = 10.dp,
                iconSize = 28.dp,
                buttonSize = 40.dp,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.stop)
            )
        }

        Button(
            onClick = { if (isTracking) onPause() else onResume() },
            modifier = Modifier
                .height(40.dp)
                .wrapContentSize()
        ) {
            Text(text = if (isTracking) stringResource(R.string.pause) else stringResource(R.string.resume))
        }

        CircleIconButton(
            onClick = {

            },
            imageVector = Icons.Outlined.Map,
            iconColor = MaterialTheme.colorScheme.onPrimary,
            elevation = 10.dp,
            iconSize = 28.dp,
            buttonSize = 40.dp,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentDescription = "Map view"
        )
    }
}