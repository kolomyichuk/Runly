package kolomyichuk.runly.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.GoogleMap
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.ButtonStart
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.utils.TrackingUtility
import kolomyichuk.runly.utils.pauseTrackingService
import kolomyichuk.runly.utils.resumeTrackingService
import kolomyichuk.runly.utils.startRunTrackingService
import kolomyichuk.runly.utils.stopTrackingService
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RunScreen() {
    val context = LocalContext.current
    val timeInMillisState = remember { mutableLongStateOf(0L) }
    val isTrackingState = rememberSaveable { mutableStateOf(false) }
    val isPauseState = rememberSaveable { mutableStateOf(false) }
    var hasNotificationPermission by remember { mutableStateOf(true) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) startRunTrackingService(context = context)
    }

    LaunchedEffect(Unit) {
        RunTrackingService.isTracking.collectLatest { isTrackingState.value = it }
    }
    LaunchedEffect(Unit) {
        RunTrackingService.isPause.collectLatest { isPauseState.value = it }
    }
    LaunchedEffect(Unit) {
        RunTrackingService.timeInMillis.collectLatest { timeInMillisState.longValue = it }
    }

    val formattedTime = TrackingUtility.formatTime(timeInMillisState.longValue)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "0 km",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "Distance",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "--:-- /km",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Current Pace",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = formattedTime,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Time",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }

        if (!isTrackingState.value && !isPauseState.value) {
            ButtonStart(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            context,
                            "android.permission.POST_NOTIFICATIONS"

                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                    } else startRunTrackingService(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                text = "Start",
                roundDp = 8.dp
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isTrackingState.value || isPauseState.value) {
                        CircleIconButton(
                            onClick = {
                                stopTrackingService(context = context)
                            },
                            imageVector = Icons.Filled.Stop,
                            iconColor = MaterialTheme.colorScheme.onSurface,
                            elevation = 10.dp,
                            iconSize = 28.dp,
                            buttonSize = 40.dp,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            contentDescription = "Stop"
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isTrackingState.value) {
                        Button(
                            onClick = {
                                pauseTrackingService(context = context)
                            },
                            modifier = Modifier
                                .width(130.dp)
                                .height(40.dp)
                        ) {
                            Text(text = "Pause")
                        }
                    } else if (isPauseState.value) {
                        Button(
                            onClick = {
                                resumeTrackingService(context = context)
                            },
                            modifier = Modifier
                                .width(130.dp)
                                .height(40.dp)
                        ) {
                            Text(text = "Resume")
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircleIconButton(
                        onClick = {

                        },
                        imageVector = Icons.Outlined.LocationOn,
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        elevation = 10.dp,
                        iconSize = 28.dp,
                        buttonSize = 40.dp,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentDescription = "Map view"
                    )
                }
            }
        }
    }
}
