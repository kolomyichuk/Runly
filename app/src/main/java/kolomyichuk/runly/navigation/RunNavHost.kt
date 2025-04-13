package kolomyichuk.runly.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.screens.DashboardScreen
import kolomyichuk.runly.ui.screens.run.RunScreen
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun RunNavHost() {
    val runNavController = rememberNavController()

    NavHost(
        navController = runNavController,
        startDestination = "run_graph"
    ) {
        composable("run_graph") {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            RunScreen(
                navController = runNavController,
                themeViewModel = themeViewModel
            )
        }
        composable("dashboard"){
            DashboardScreen(
                navController = runNavController
            )
        }
    }
}