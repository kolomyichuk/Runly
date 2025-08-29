package kolomyichuk.runly.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.ext.getUnitLabel

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    profileViewModel: ProfileViewModel
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.profile),
                menuIcon = Icons.Outlined.Settings,
                onMenuClick = onNavigateToSettings
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ProfileScreenContent(
            profileViewModel = profileViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

}

@Composable
private fun ProfileScreenContent(
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val profile by profileViewModel.userProfile.collectAsStateWithLifecycle()
    val totalDistance by profileViewModel.totalDistance.collectAsStateWithLifecycle()
    val distanceUnit by profileViewModel.distanceUnit.collectAsStateWithLifecycle()
    val thisWeekDistanceByDay by profileViewModel.thisWeekDistanceByDay
        .collectAsStateWithLifecycle()

    val unitLabel = stringResource(id = distanceUnit.getUnitLabel())

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        ProfileUserInfo(
            profile = profile
        )

        ProvideVicoTheme(rememberM3VicoTheme()) {
            ThisWeekDistanceByDayChart(
                thisWeekDistanceByDay = thisWeekDistanceByDay,
                unitLabel = unitLabel
            )
        }

        Text(
            text = stringResource(R.string.total_distance_all_time, totalDistance, unitLabel),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 25.dp)
        )
    }
}
