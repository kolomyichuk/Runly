package kolomyichuk.runly.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kolomyichuk.runly.ui.screens.details.DetailsScreen
import kolomyichuk.runly.ui.screens.details.DetailsViewModel
import kolomyichuk.runly.ui.screens.home.HomeScreen
import kolomyichuk.runly.ui.screens.home.HomeViewModel

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation<HomeScreens>(startDestination = Screen.Home) {
        composable<Screen.Home> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                onRunClick = { runId ->
                    navController.navigate(Screen.Details(runId))
                }
            )
        }
        composable<Screen.Details> {
            val arg = it.toRoute<Screen.Details>()
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            DetailsScreen(
                onBack = { navController.popBackStack() },
                detailsViewModel = detailsViewModel,
                runId = arg.runId
            )
        }
    }
}