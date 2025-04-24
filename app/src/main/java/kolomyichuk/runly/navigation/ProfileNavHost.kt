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
                    profileNavController.navigate("settings")
                },
                viewModel = profileViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateToTheme = {
                    profileNavController.navigate("theme")
                },
                onBack = {
                    profileNavController.popBackStack()
                }
            )
        }
        composable("theme") {
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
