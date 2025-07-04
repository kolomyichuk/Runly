package kolomyichuk.runly.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kolomyichuk.runly.ui.screens.run.SaveRunDialog
import kolomyichuk.runly.ui.screens.run.sendCommandToRunService

@Composable
fun DashboardButtonsBlock(
    navController: NavController,
    isTracking: Boolean,
    isActiveRun: Boolean,
    distance: String,
    onSaveRun: () -> Unit
) {
    var showSaveRunDialog by remember { mutableStateOf(false) }

    if (showSaveRunDialog) {
        SaveRunDialog(
            distance = distance,
            onSaveRun = onSaveRun,
            onDismiss = { showSaveRunDialog = false },
            navController = navController
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current

        if (isActiveRun) {
            StopButton {
                sendCommandToRunService(
                    context = context, route = RunTrackingService.ACTION_PAUSE_TRACKING
                )
                showSaveRunDialog = true
            }
        }

        Button(
            onClick = {
                if (isTracking) {
                    sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_PAUSE_TRACKING
                    )
                } else {
                    sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_RESUME_TRACKING
                    )
                }
            },
            modifier = Modifier
                .height(40.dp)
                .wrapContentSize()
        ) {
            Text(
                text = if (isTracking) stringResource(R.string.pause)
                else stringResource(R.string.resume)
            )
        }
        MapVisibilityButton { navController.popBackStack() }
    }
}
