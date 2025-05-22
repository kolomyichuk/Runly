package kolomyichuk.runly.ui.screens.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.Q)
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

    val foregroundPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val backgroundPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION

    var showForegroundLocationDialog by remember { mutableStateOf(false) }
    var isForegroundLocationGranted by remember { mutableStateOf(false) }

    var isBackgroundLocationGranted by remember { mutableStateOf(false) }
    var showBackgroundLocationDialog by remember { mutableStateOf(false) }

    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isForegroundLocationGranted = permissions.values.all { it }
        if (!isForegroundLocationGranted){
            val shouldShowRationale = foregroundPermissions.any { permission ->
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
            }
            if (!shouldShowRationale) {
                showForegroundLocationDialog = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.foreground_location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isBackgroundLocationGranted = granted
        if (!granted) {
            val shouldShowRationale =
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    backgroundPermission
                )
            if (!shouldShowRationale) {
                showBackgroundLocationDialog = true
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.background_location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        isForegroundLocationGranted = foregroundPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!isForegroundLocationGranted) {
            foregroundLocationLauncher.launch(foregroundPermissions)
        }
    }

    LaunchedEffect(isForegroundLocationGranted) {
        if (isForegroundLocationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val granted = ContextCompat.checkSelfPermission(
                context,
                backgroundPermission
            ) == PackageManager.PERMISSION_GRANTED
            isBackgroundLocationGranted = granted
            if (!granted){
                backgroundLocationLauncher.launch(backgroundPermission)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val granted = foregroundPermissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }
                isForegroundLocationGranted = granted

                if (!granted) {
                    val shouldShowRationale = foregroundPermissions.any { permission ->
                        activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    }

                    if (!shouldShowRationale) {
                        showForegroundLocationDialog = true
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showBackgroundLocationDialog) {
        BackgroundLocationDialog(
            onDismiss = { showBackgroundLocationDialog = false }
        )
    }
    if (showForegroundLocationDialog) {
        ForegroundLocationDialog(
            onDismiss = { showForegroundLocationDialog = false })
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
