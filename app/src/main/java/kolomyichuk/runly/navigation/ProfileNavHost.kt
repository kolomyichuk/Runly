package kolomyichuk.runly.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.screens.ProfileScreen
import kolomyichuk.runly.ui.screens.SettingsScreen
import kolomyichuk.runly.ui.screens.ThemeScreen
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun ProfileNavHost() {
    val profileNavController = rememberNavController()

    NavHost(
        navController = profileNavController,
        startDestination = "profile_graph"
    ) {
        composable("profile_graph") {
            ProfileScreen(onNavigateToSettings = {
                profileNavController.navigate("settings")
            })
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
            val viewModel = hiltViewModel<ThemeViewModel>()
            ThemeScreen(
                onBack = {
                    profileNavController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
