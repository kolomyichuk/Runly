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
import androidx.compose.runtime.MutableState
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
    val isTracking = rememberSaveable { mutableStateOf(false) }
    val isPause = rememberSaveable { mutableStateOf(false) }
    var hasNotificationPermission by remember { mutableStateOf(true) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) startRunTrackingService(context = context)
    }

    LaunchedEffect(Unit) {
        RunTrackingService.isTracking.collectLatest { isTracking.value = it }
    }
    LaunchedEffect(Unit) {
        RunTrackingService.isPause.collectLatest { isPause.value = it }
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
            InfoColumn("0.km", "Distance")
            InfoColumn("--:-- /km", "Current Pace")
            InfoColumn(formattedTime, "Time")
        }

        if (!isTracking.value && !isPause.value) {
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
            ControlButtons(
                isTracking = isTracking,
                isPause = isPause,
                onPause = { pauseTrackingService(context = context) },
                onResume = { resumeTrackingService(context = context) },
                onStop = { stopTrackingService(context = context) }
            )
        }
    }
}

@Composable
fun InfoColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ControlButtons(
    isTracking: MutableState<Boolean>,
    isPause: MutableState<Boolean>,
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
        if (isTracking.value || isPause.value) {
            CircleIconButton(
                onClick = { onStop() },
                imageVector = Icons.Filled.Stop,
                iconColor = MaterialTheme.colorScheme.onSurface,
                elevation = 10.dp,
                iconSize = 28.dp,
                buttonSize = 40.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentDescription = "Stop"
            )
        }

        Button(
            onClick = { if (isTracking.value) onPause() else onResume() },
            modifier = Modifier
                .width(130.dp)
                .height(40.dp)
        ) {
            Text(text = if (isTracking.value) "Pause" else "Resume")
        }

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
