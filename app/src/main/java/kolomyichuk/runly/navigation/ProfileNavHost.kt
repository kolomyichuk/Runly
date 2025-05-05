package kolomyichuk.runly.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.screens.profile.ProfileScreen
import kolomyichuk.runly.ui.screens.settings.SettingsScreen
import kolomyichuk.runly.ui.screens.theme.ThemeScreen
import kolomyichuk.runly.ui.viewmodel.ProfileViewModel
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun ProfileNavHost() {
    val profileNavController = rememberNavController()

    NavHost(
        navController = profileNavController,
        startDestination = "profile_graph"
    ) {
        composable("profile_graph") {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onNavigateToSettings = {
                    profileNavController.navigate(Screen.Settings)
                },
                profileViewModel = profileViewModel
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateToTheme = {
                    profileNavController.navigate(Screen.Theme)
                },
                onBack = {
                    profileNavController.popBackStack()
                }
            )
        }
        composable<Screen.Theme> {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            ThemeScreen(
                onBack = {
                    profileNavController.popBackStack()
                },
                viewModel = themeViewModel
            )
        }
    }
}
