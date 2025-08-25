package kolomyichuk.runly.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

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

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        ProfileUserInfo(
            profile = profile
        )
    }
}
