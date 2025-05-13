package kolomyichuk.runly.ui.screens.dashboard

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.screens.run.RunViewModel
import kolomyichuk.runly.utils.TrackingUtility

@Composable
fun DashboardScreen(
    navController: NavController,
    runViewModel: RunViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBarApp(
            title = stringResource(R.string.dashboard),
            onBackClick = { navController.popBackStack() }
        )
        ContentDashboardScreen(navController = navController, runViewModel = runViewModel)
    }
}

@Composable
private fun ContentDashboardScreen(
    navController: NavController,
    runViewModel: RunViewModel
) {
    val runState = runViewModel.runState.collectAsStateWithLifecycle()
    val formattedTime = TrackingUtility.formatTime(runState.value.timeInMillis)
    val formattedDistance = TrackingUtility.formatDistanceToKm(runState.value.distanceInMeters)
    val avgSpeed = runState.value.avgSpeed.toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoBlock(
                title = stringResource(R.string.time),
                value = formattedTime,
                type = " "
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(5.dp))

            InfoBlock(
                title = stringResource(R.string.avg_speed),
                value = avgSpeed,
                type = stringResource(R.string.km_h)
            )

            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(5.dp))

            InfoBlock(
                title = stringResource(R.string.distance),
                value = formattedDistance,
                type = stringResource(R.string.kilometers)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            val context = LocalContext.current

            if (runState.value.isTracking || runState.value.isPause) {
                CircleIconButton(
                    onClick = {
                        sendCommandToRunService(
                            context = context,
                            route = RunTrackingService.ACTION_STOP_TRACKING
                        )
                        navController.popBackStack()
                    },
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
                onClick = {
                    if (runState.value.isTracking) sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_PAUSE_TRACKING
                    ) else sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_RESUME_TRACKING
                    )
                },
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = if (runState.value.isTracking) stringResource(R.string.pause)
                    else stringResource(R.string.resume)
                )
            }

            CircleIconButton(
                onClick = { navController.popBackStack() },
                imageVector = Icons.Outlined.Map,
                iconColor = MaterialTheme.colorScheme.onPrimary,
                elevation = 10.dp,
                iconSize = 28.dp,
                buttonSize = 40.dp,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentDescription = "Map hide"
            )
        }
    }
}

private fun sendCommandToRunService(context: Context, route: String) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = route
    }
    context.startService(intent)
}