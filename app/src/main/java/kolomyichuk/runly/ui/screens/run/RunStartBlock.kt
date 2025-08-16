@file:OptIn(ExperimentalPermissionsApi::class)

package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.StartButton

@Composable
fun RunStartBlock(
    hasForegroundLocationPermission: Boolean,
    isTracking: Boolean,
    isPause: Boolean,
    distance: String,
    navController: NavController,
    onSaveRun: () -> Unit
) {
    val context = LocalContext.current

    val backgroundPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    var showBackgroundLocationDialog by remember { mutableStateOf(false) }

    if (showBackgroundLocationDialog) {
        BackgroundLocationDialog(
            onDismiss = { showBackgroundLocationDialog = false }
        )
    }

    if (!isTracking && !isPause) {
        StartButton(
            onClick = {
                handleStartClick(
                    context = context,
                    hasForegroundLocationPermission = hasForegroundLocationPermission,
                    backgroundPermissionState = backgroundPermissionState,
                    onShowDialog = { showBackgroundLocationDialog = true }
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
            distance = distance,
            navController = navController,
            onSaveRun = onSaveRun
        )
    }
}


private fun handleStartClick(
    context: Context,
    hasForegroundLocationPermission: Boolean,
    backgroundPermissionState: PermissionState,
    onShowDialog: () -> Unit
) {
    if (hasForegroundLocationPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            when (val status = backgroundPermissionState.status) {
                is PermissionStatus.Granted -> {
                    sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_START_TRACKING
                    )
                }

                is PermissionStatus.Denied -> {
                    if (status.shouldShowRationale) {
                        onShowDialog()
                    }
                }

                else -> {
                    onShowDialog()
                }
            }
        } else {
            sendCommandToRunService(
                context = context,
                route = RunTrackingService.ACTION_START_TRACKING
            )
        }

    } else {
        Toast.makeText(
            context,
            context.getString(R.string.location_permission_not_granted),
            Toast.LENGTH_SHORT
        ).show()
    }
}


fun sendCommandToRunService(context: Context, route: String) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = route
    }
    context.startService(intent)
}
