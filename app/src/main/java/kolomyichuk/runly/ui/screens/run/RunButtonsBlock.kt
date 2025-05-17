package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.MapVisibilityButton
import kolomyichuk.runly.ui.components.StartButton
import kolomyichuk.runly.ui.components.StopButton
import kolomyichuk.runly.ui.navigation.Screen

@Composable
fun RunButtonsBlock(
    isTracking: Boolean,
    isPause: Boolean,
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    var isBackgroundGranted by remember { mutableStateOf(false) }
    var showBackgroundLocationDialog by remember { mutableStateOf(false) }

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isBackgroundGranted = granted
        if (!granted) {
            val shouldShowRationale =
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            if (!shouldShowRationale) {
                showBackgroundLocationDialog = true
            } else {
                Toast.makeText(context, "Беграунд дозвіл відхилено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        isBackgroundGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    if (!isTracking && !isPause) {
        StartButton(
            onClick = {
                if (isBackgroundGranted) {
                    sendCommandToRunService(
                        context = context,
                        route = RunTrackingService.ACTION_START_TRACKING
                    )
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    } else {
                        sendCommandToRunService(
                            context = context,
                            route = RunTrackingService.ACTION_START_TRACKING
                        )
                    }
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
            navController = navController
        )
    }

    if (showBackgroundLocationDialog) {
        AlertDialog(
            onDismissRequest = { showBackgroundLocationDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showBackgroundLocationDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
                ) {
                    Text(text = "Перейти до налаштувань")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showBackgroundLocationDialog = false
                    }
                ) {
                    Text(text = "Скасувати")
                }
            },
            title = { Text(text = "Дозвіл на фонову локацію") },
            text = { Text(text = "Для запису пробіжки у фоновому режимі потрібен додатковий дозвіл на локацію, а саме потрібно - дозволити локацію завжди") }
        )
    }
}

@Composable
private fun OtherButtons(
    isTracking: Boolean,
    isPause: Boolean,
    navController: NavController,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        if (isTracking || isPause) {
            StopButton {
                sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_STOP_TRACKING
                )
            }
        }

        Button(
            onClick = {
                if (isTracking) sendCommandToRunService(
                    context = context, route = RunTrackingService.ACTION_PAUSE_TRACKING
                ) else sendCommandToRunService(
                    context = context,
                    route = RunTrackingService.ACTION_RESUME_TRACKING
                )
            },
            modifier = Modifier
                .height(40.dp)
                .wrapContentSize()
        ) {
            Text(text = if (isTracking) stringResource(R.string.pause) else stringResource(R.string.resume))
        }

        MapVisibilityButton { navController.navigate(Screen.Dashboard) }
    }
}

fun sendCommandToRunService(context: Context, route: String) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = route
    }
    context.startService(intent)
}