package kolomyichuk.runly.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.profile),
            menuIcon = Icons.Outlined.Settings,
            onMenuClick = onNavigateToSettings
        )
        ContentProfileScreen(
            profileViewModel = profileViewModel
        )
    }

}

@Composable
fun ContentProfileScreen(
    profileViewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserInfo(
            profileViewModel = profileViewModel
        )
    }
}