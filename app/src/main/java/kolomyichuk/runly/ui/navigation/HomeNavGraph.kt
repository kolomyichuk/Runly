package kolomyichuk.runly.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kolomyichuk.runly.ui.screens.rundetails.RunDetailsScreen
import kolomyichuk.runly.ui.screens.rundetails.RunDetailsViewModel
import kolomyichuk.runly.ui.screens.home.HomeScreen
import kolomyichuk.runly.ui.screens.home.HomeViewModel

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation<HomeScreens>(startDestination = Screen.Home) {
        composable<Screen.Home> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                onRunClick = { runId ->
                    navController.navigate(Screen.RunDetails(runId))
                }
            )
        }
        composable<Screen.RunDetails> {
            val arg = it.toRoute<Screen.RunDetails>()
            val runDetailsViewModel: RunDetailsViewModel = hiltViewModel()
            RunDetailsScreen(
                onBack = { navController.popBackStack() },
                runDetailsViewModel = runDetailsViewModel,
                runId = arg.runId
            )
        }
    }
}