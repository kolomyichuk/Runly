package kolomyichuk.runly.ui.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.HorizontalLineDivider
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.utils.FormatterUtils
import kolomyichuk.runly.utils.FormatterUtils.toFormattedDateTime

@Composable
fun DetailsScreen(
    onBack: () -> Unit,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    runId: Int
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.details),
            onBackClick = onBack
        )
        DetailsScreenContent(
            detailsViewModel = detailsViewModel,
            runId = runId
        )
    }
}

@Composable
private fun DetailsScreenContent(
    detailsViewModel: DetailsViewModel,
    runId: Int
) {
    LaunchedEffect(runId) {
        detailsViewModel.loadRun(runId)
    }

    val run by detailsViewModel.run.collectAsStateWithLifecycle()

    val formattedDistance by remember(run) {
        derivedStateOf {
            run?.let {
                FormatterUtils.formatDistanceToKm(it.distanceInMeters)
            } ?: ""
        }
    }

    val avgSpeed by remember(run) {
        derivedStateOf {
            run?.avgSpeed ?: 0f
        }
    }

    val formattedTime by remember(run) {
        derivedStateOf {
            run?.let {
                FormatterUtils.formatTime(it.durationInMillis)
            } ?: ""
        }
    }

    val timestamp by remember(run) {
        derivedStateOf {
            run?.let {
                run?.timestamp?.toFormattedDateTime()
            } ?: ""
        }
    }

    val pathPoints by remember {
        derivedStateOf {
            run?.routePoints?.map { path ->
                path.map { point ->
                    LatLng(point.latitude, point.longitude)
                }
            } ?: emptyList()
        }
    }

    if (run != null) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp))
        {
            DetailsHeaderBlock(timestamp = timestamp)
            HorizontalLineDivider()
            DetailsInfoBlock(
                distance = formattedDistance,
                avgSpeed = avgSpeed,
                time = formattedTime
            )
            DetailsMapWithRoute(
                pathPoints = pathPoints,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}