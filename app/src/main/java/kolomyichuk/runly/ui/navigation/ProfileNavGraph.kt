package kolomyichuk.runly.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kolomyichuk.runly.ui.screens.profile.ProfileScreen
import kolomyichuk.runly.ui.screens.settings.SettingsScreen
import kolomyichuk.runly.ui.screens.theme.ThemeScreen
import kolomyichuk.runly.ui.screens.profile.ProfileViewModel
import kolomyichuk.runly.ui.screens.theme.ThemeViewModel

fun NavGraphBuilder.profileNavGraph(navController: NavController){
    navigation<ProfileScreens>(startDestination = Screen.Profile){
        composable<Screen.Profile>{
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                },
                profileViewModel = profileViewModel
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateToTheme = {
                    navController.navigate(Screen.Theme)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.Theme> {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            ThemeScreen(
                onBack = {
                    navController.popBackStack()
                },
                viewModel = themeViewModel
            )
        }
    }
}
