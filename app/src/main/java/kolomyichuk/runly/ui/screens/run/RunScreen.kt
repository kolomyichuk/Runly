package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun RunScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.run),
            onBackClick = { navController.popBackStack() }
        )
        val theme by themeViewModel.themeFlow.collectAsState()

        val isDarkTheme = when (theme) {
            AppTheme.DARK -> true
            AppTheme.LIGHT -> false
            AppTheme.SYSTEM -> isSystemInDarkTheme()
        }
        ContentRunScreen(isDarkTheme = isDarkTheme)

        val context = LocalContext.current
        val activity = context as? android.app.Activity
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        var showSettingsDialog by remember { mutableStateOf(false) }
        var isLocationGranted by remember { mutableStateOf(false) }

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            isLocationGranted = allGranted

            val shouldShowRationale = locationPermissions.any { permission ->
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
            }
            if (!shouldShowRationale) {
                showSettingsDialog = true
            } else {
                Toast.makeText(context, "Дозвіл на локацію відхилено", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            isLocationGranted = locationPermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }

        LaunchedEffect(!isLocationGranted) {
            locationPermissionLauncher.launch(locationPermissions)
        }

        AlertDialog(
            onDismissRequest = {
                showSettingsDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text(text = "Відкрити налаштування")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                }) {
                    Text(text = "Скасувати")
                }
            },
            title = { Text("Дозвіл заблоковано") },
            text = {
                Text("Ви відхилили дозвіл і обрали 'Більше не питати'. Щоб надати доступ, відкрийте налаштування додатку.")
            }
        )
    }
}

@Composable
fun ContentRunScreen(isDarkTheme: Boolean) {
    val timeInMillis by RunTrackingService.timeInMillis.collectAsState(0L)
    val isTracking by RunTrackingService.isTracking.collectAsState(initial = false)
    val isPause by RunTrackingService.isPause.collectAsState(initial = false)
    val pathPoints by RunTrackingService.pathPoints.collectAsState(emptyList())
    val distanceInMeters by RunTrackingService.distanceInMeters.collectAsState(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RunMapView(
            pathPoints = pathPoints,
            isDarkTheme = isDarkTheme,
            modifier = Modifier.weight(1f)
        )

        InfoPanel(
            distanceInMeters = distanceInMeters,
            timeInMillis = timeInMillis
        )

        ControlButtonsPanel(
            isTracking = isTracking,
            isPause = isPause
        )
    }
}
