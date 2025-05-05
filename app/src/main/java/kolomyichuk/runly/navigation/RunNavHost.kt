package kolomyichuk.runly.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.screens.dashboard.DashboardScreen
import kolomyichuk.runly.ui.screens.run.RunScreen
import kolomyichuk.runly.ui.viewmodel.RunViewModel
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun RunNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "run_graph"
    ) {
        composable("run_graph") {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val runViewModel = hiltViewModel<RunViewModel>()
            RunScreen(
                navController = navController,
                themeViewModel = themeViewModel,
                runViewModel = runViewModel
            )
        }
        composable<Screen.Dashboard> {
            DashboardScreen(
                navController = navController
            )
        }
    }
}