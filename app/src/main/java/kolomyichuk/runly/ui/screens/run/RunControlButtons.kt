package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.MapVisibilityButton
import kolomyichuk.runly.ui.components.StopButton
import kolomyichuk.runly.ui.navigation.Screen

@Composable
fun RunControlButtons(
    isTracking: Boolean,
    isPause: Boolean,
    navController: NavController,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        if (isTracking || isPause) {
            StopButton {
                sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_STOP_TRACKING
                )
            }
        }

        Button(
            onClick = {
                if (isTracking) sendCommandToRunService(
                    context = context, route = RunTrackingService.ACTION_PAUSE_TRACKING
                ) else sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_RESUME_TRACKING
                )
            },
            modifier = Modifier
                .height(40.dp)
                .wrapContentSize()
        ) {
            Text(text = if (isTracking) stringResource(R.string.pause) else stringResource(R.string.resume))
        }

        MapVisibilityButton { navController.navigate(Screen.Dashboard) }
    }
}