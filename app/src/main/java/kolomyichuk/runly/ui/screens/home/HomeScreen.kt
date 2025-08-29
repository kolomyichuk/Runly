package kolomyichuk.runly.ui.screens.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onRunClick: (String) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionsState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(Unit) {
            notificationPermissionsState.launchPermissionRequest()
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.homeEffects.collect { effect ->
            when (effect) {
                is HomeEffect.ShowDeleteSnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = context.getString(R.string.run_deleted),
                        actionLabel = context.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        homeViewModel.undoDeleteRun()
                    } else {
                        homeViewModel.confirmDeleteRun()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopBarApp(
                title = stringResource(R.string.home)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        HomeScreenContent(
            homeViewModel = homeViewModel,
            onRunClick = onRunClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
