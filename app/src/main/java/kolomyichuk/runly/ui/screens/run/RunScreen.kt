package kolomyichuk.runly.ui.screens.run

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
