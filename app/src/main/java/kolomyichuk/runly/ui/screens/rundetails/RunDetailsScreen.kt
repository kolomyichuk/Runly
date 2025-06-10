package kolomyichuk.runly.ui.screens.rundetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun RunDetailsScreen(
    onBack: () -> Unit,
    runDetailsViewModel: RunDetailsViewModel = hiltViewModel(),
    runId: Int
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.details),
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        RunDetailsScreenContent(
            runDetailsViewModel = runDetailsViewModel,
            runId = runId,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun RunDetailsScreenContent(
    runDetailsViewModel: RunDetailsViewModel,
    runId: Int,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(runId) {
        runDetailsViewModel.loadRun(runId)
    }

    val run by runDetailsViewModel.runDetailsState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        RunDetailsHeaderBlock(dateTime = run.dateTime)
        HorizontalLineDivider()
        RunDetailsInfoBlock(
            distance = run.distance,
            avgSpeed = run.avgSpeed,
            duration = run.duration,
            unit = run.unit
        )
        RunDetailsMapWithRoute(
            pathPoints = run.routePoints,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}