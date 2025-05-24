package kolomyichuk.runly.ui.screens.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleForegroundPermissions(
    foregroundPermissionState: MultiplePermissionsState,
) {
    var showForegroundLocationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (foregroundPermissionState.shouldShowRationale) {
            showForegroundLocationDialog = true
        } else {
            foregroundPermissionState.launchMultiplePermissionRequest()
        }
    }

    if (showForegroundLocationDialog) {
        ForegroundLocationDialog(
            onDismiss = { showForegroundLocationDialog = false }
        )
    }
}