package kolomyichuk.runly.ui.screens.run

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.StartButton

@Composable
fun RunStartBlock(
    isTracking: Boolean,
    isPause: Boolean,
    navController: NavController
) {
    val context = LocalContext.current

    if (!isTracking && !isPause) {
        StartButton(
            onClick = {
                sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_START_TRACKING
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            text = stringResource(R.string.start),
            roundDp = 8.dp
        )
    } else {
        RunControlButtons(
            isTracking = isTracking,
            isPause = isPause,
            navController = navController
        )
    }
}

fun sendCommandToRunService(context: Context, route: String) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = route
    }
    context.startService(intent)
}