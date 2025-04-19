package kolomyichuk.runly.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.utils.Constants
import kolomyichuk.runly.utils.TrackingUtility

@Composable
fun DashboardScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBarApp(
            title = "Dashboard",
            onBackClick = { navController.popBackStack() }
        )
        ContentDashboardScreen(navController = navController)
    }
}

@Composable
fun ContentDashboardScreen(
    navController: NavController
) {
    val timeInMillis by RunTrackingService.timeInMillis.collectAsStateWithLifecycle(initialValue = 0L)
    val isTracking by RunTrackingService.isTracking.collectAsStateWithLifecycle(initialValue = false)
    val isPause by RunTrackingService.isPause.collectAsStateWithLifecycle(initialValue = false)
    val distanceInMeters by RunTrackingService.distanceInMeters.collectAsStateWithLifecycle(0.0)
    val avgSpeed by RunTrackingService.avgSpeed.collectAsStateWithLifecycle(0.00f)
    val formattedTime = TrackingUtility.formatTime(timeInMillis)
    val formattedDistance = "%.2f".format(distanceInMeters / 1000)

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
            Text(
                text = stringResource(R.string.time),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = formattedTime,
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Avg Speed",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$avgSpeed",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "KM/H",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.distance),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = formattedDistance,
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "KILOMETERS",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
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

            if (isTracking || isPause) {
                CircleIconButton(
                    onClick = {
                        sendCommandToRunService(
                            context = context,
                            route = Constants.ACTION_STOP_TRACKING
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
                    if (isTracking) sendCommandToRunService(
                        context = context,
                        route = Constants.ACTION_PAUSE_TRACKING
                    ) else sendCommandToRunService(
                        context = context,
                        route = Constants.ACTION_RESUME_TRACKING
                    )
                },
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentSize()
            ) {
                Text(text = if (isTracking) stringResource(R.string.pause) else stringResource(R.string.resume))
            }

            CircleIconButton(
                onClick = {
                    navController.popBackStack()
                },
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