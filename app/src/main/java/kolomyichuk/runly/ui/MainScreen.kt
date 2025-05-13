package kolomyichuk.runly.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.navigation.profileNavGraph
import kolomyichuk.runly.ui.navigation.runNavGraph
import kolomyichuk.runly.ui.screens.home.HomeScreen
import kolomyichuk.runly.ui.screens.home.HomeViewModel
import kolomyichuk.runly.ui.screens.run.RunViewModel

@Composable
fun MainScreen(
    navController: NavHostController
) {
    val runViewModel: RunViewModel = hiltViewModel()
    val runState by runViewModel.runState.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = {
            val currentEntry by navController.currentBackStackEntryAsState()
            if (currentEntry?.destination?.hasRoute<Screen.Run>() == false &&
                currentEntry?.destination?.hasRoute<Screen.Dashboard>() == false
            ) {
                BottomNavigationBar(navController, runState.isActiveRun)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.Home> {
                val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    homeViewModel = homeViewModel
                )
            }
            runNavGraph(navController)
            profileNavGraph(navController)
        }
    }
}