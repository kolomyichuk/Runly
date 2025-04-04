package kolomyichuk.runly.ui.screens.run

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
){
    val context = LocalContext.current

    var hasNotificationPermission by remember { mutableStateOf(true) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions["android.permission.ACCESS_FINE_LOCATION"] == true &&
                permissions["android.permission.ACCESS_COARSE_LOCATION"] == true
        val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions["android.permission.POST_NOTIFICATIONS"] == true
        } else true

        hasNotificationPermission = notificationGranted
        if (locationGranted) {
            startRunTrackingService(context = context)
        }
    }

    if (!isTracking && !isPause) {
        ButtonStart(
            onClick = {
                val permissionsToRequest = mutableListOf<String>()

                if (ContextCompat.checkSelfPermission(
                        context, "android.permission.ACCESS_FINE_LOCATION"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add("android.permission.ACCESS_FINE_LOCATION")
                }
                if (ContextCompat.checkSelfPermission(
                        context, "android.permission.ACCESS_COARSE_LOCATION"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add("android.permission.ACCESS_COARSE_LOCATION")
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        context, "android.permission.POST_NOTIFICATIONS"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add("android.permission.POST_NOTIFICATIONS")
                }

                if (permissionsToRequest.isNotEmpty()) {
                    permissionsLauncher.launch(permissionsToRequest.toTypedArray())
                } else {
                    startRunTrackingService(context = context)
                }
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
            onClick = {},
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