@file:OptIn(ExperimentalPermissionsApi::class)

package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import kolomyichuk.runly.domain.run.model.RunDisplayModel
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.StartButton

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RunStartBlock(
    hasForegroundLocationPermission: Boolean,
    runState: RunDisplayModel,
    navController: NavController,
    onSaveRun: () -> Unit
) {
    val context = LocalContext.current

    val backgroundLocationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val notificationPermissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    var showBackgroundLocationDialog by remember { mutableStateOf(false) }
    var showNotificationPermissionDialog by remember { mutableStateOf(false) }

    if (showBackgroundLocationDialog) {
        BackgroundLocationDialog(
            onDismiss = { showBackgroundLocationDialog = false }
        )
    }

    if (showNotificationPermissionDialog) {
        NotificationPermissionDialog(
            onDismiss = { showNotificationPermissionDialog = false }
        )
    }

    if (!runState.isTracking && !runState.isPause) {
        StartButton(
            onClick = {
                handleStartClick(
                    context = context,
                    hasForegroundLocationPermission = hasForegroundLocationPermission,
                    backgroundPermissionState = backgroundLocationPermissionState,
                    onShowBackgroundLocationDialog = { showBackgroundLocationDialog = true },
                    notificationPermissionState = notificationPermissionState,
                    onShowNotificationPermissionDialog = { showNotificationPermissionDialog = true }
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
            isTracking = runState.isTracking,
            isPause = runState.isPause,
            distance = runState.distance,
            navController = navController,
            onSaveRun = onSaveRun
        )
    }
}


private fun handleStartClick(
    context: Context,
    hasForegroundLocationPermission: Boolean,
    backgroundPermissionState: PermissionState,
    onShowBackgroundLocationDialog: () -> Unit = {},
    notificationPermissionState: PermissionState,
    onShowNotificationPermissionDialog: () -> Unit = {}
) {
    if (hasForegroundLocationPermission) {
        handleBackgroundLocationAndNotificationPermission(
            context = context,
            backgroundPermissionState = backgroundPermissionState,
            onShowBackgroundLocationDialog = onShowBackgroundLocationDialog,
            notificationPermissionState = notificationPermissionState,
            onShowNotificationPermissionDialog = onShowNotificationPermissionDialog
        )
    } else {
        Toast.makeText(
            context,
            context.getString(R.string.location_permission_not_granted),
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun handleBackgroundLocationAndNotificationPermission(
    context: Context,
    backgroundPermissionState: PermissionState,
    onShowBackgroundLocationDialog: () -> Unit,
    notificationPermissionState: PermissionState,
    onShowNotificationPermissionDialog: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        when (val status = backgroundPermissionState.status) {
            is PermissionStatus.Granted -> {
                checkNotificationPermission(
                    context = context,
                    notificationPermissionState = notificationPermissionState,
                    onShowNotificationPermissionDialog = onShowNotificationPermissionDialog
                )
            }

            is PermissionStatus.Denied -> {
                if (status.shouldShowRationale) {
                    onShowBackgroundLocationDialog()
                }
            }

            else -> {
                onShowBackgroundLocationDialog()
            }
        }
    } else {
        sendCommandToRunService(
            context = context,
            route = RunTrackingService.ACTION_START_TRACKING
        )
    }
}

fun checkNotificationPermission(
    context: Context,
    notificationPermissionState: PermissionState,
    onShowNotificationPermissionDialog: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when (notificationPermissionState.status) {
            is PermissionStatus.Granted -> {
                sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_START_TRACKING
                )
            }

            is PermissionStatus.Denied -> {
                onShowNotificationPermissionDialog()
            }

            else -> {
                onShowNotificationPermissionDialog()
            }
        }

    } else {
        sendCommandToRunService(
            context = context,
            route = RunTrackingService.ACTION_START_TRACKING
        )
    }
}


fun sendCommandToRunService(context: Context, route: String) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = route
    }
    context.startService(intent)
}
