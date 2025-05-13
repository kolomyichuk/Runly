package kolomyichuk.runly.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kolomyichuk.runly.ui.screens.dashboard.DashboardScreen
import kolomyichuk.runly.ui.screens.run.RunScreen
import kolomyichuk.runly.ui.screens.run.RunViewModel
import kolomyichuk.runly.ui.screens.theme.ThemeViewModel

fun NavGraphBuilder.runNavGraph(navController: NavController){
    navigation<RunScreens>(startDestination = Screen.Run){
        composable<Screen.Run> {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val runViewModel = hiltViewModel<RunViewModel>()
            RunScreen(
                navController = navController,
                themeViewModel = themeViewModel,
                runViewModel = runViewModel
            )
        }
        composable<Screen.Dashboard> {
            val runViewModel = hiltViewModel<RunViewModel>()
            DashboardScreen(
                navController = navController,
                runViewModel = runViewModel
            )
        }
    }
}