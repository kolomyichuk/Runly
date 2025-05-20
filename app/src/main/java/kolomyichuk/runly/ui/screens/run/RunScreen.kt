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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.TopBarApp

// TODO Let's divide this screen to improve readability
@Composable
fun RunScreen(
    navController: NavController,
    runViewModel: RunViewModel = hiltViewModel(),
) {
    val appTheme = LocalAppTheme.current

    val isDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val context = LocalContext.current
    val activity = context as? android.app.Activity
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var showSettingsDialog by remember { mutableStateOf(false) }
    var isLocationGranted by remember { mutableStateOf(false) }
    var alreadyRequestedPermission by remember { mutableStateOf(false) }

    // TODO Let's also request ACCESS_BACKGROUND_LOCATION permission here to get rid of it in RunButtonsBlock.kt
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
        if (!isLocationGranted && !alreadyRequestedPermission) {
            alreadyRequestedPermission = true
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val granted = locationPermissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }
                isLocationGranted = granted

                if (!granted) {
                    val shouldShowRationale = locationPermissions.any { permission ->
                        activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    }

                    if (!shouldShowRationale) {
                        showSettingsDialog = true
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // TODO Extract it to the separate Composable
    // TODO Use values from strings.xml file
    if (showSettingsDialog) {
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
            title = { Text("Дозвіл на локацію заблоковано") },
            text = {
                Text("Ви відхилили дозвіл на локацію і обрали 'Більше не питати'. Щоб мати можливість записати пробіжку потрібно надати доступ до локації на постійній основі у налаштуваннях.")
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.run),
            onBackClick = { navController.popBackStack() }
        )
        RunScreenContent(
            isDarkTheme = isDarkTheme,
            navController = navController,
            runViewModel = runViewModel
        )
    }
}
