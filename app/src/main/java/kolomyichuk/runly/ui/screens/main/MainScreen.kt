package kolomyichuk.runly.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.navigation.HomeScreens
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.navigation.homeNavGraph
import kolomyichuk.runly.ui.navigation.profileNavGraph
import kolomyichuk.runly.ui.navigation.runNavGraph

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val runState by mainViewModel.runState.collectAsStateWithLifecycle()

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
            startDestination = HomeScreens,
            modifier = Modifier.padding(paddingValues)
        ) {
            homeNavGraph(navController)
            runNavGraph(navController)
            profileNavGraph(navController)
        }
    }
}