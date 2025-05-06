package kolomyichuk.runly.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.home.HomeScreen
import kolomyichuk.runly.ui.viewmodel.HomeViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    startScreen: MutableState<Screen>
) {
    val isRunActive by RunTrackingService.isActiveRun.collectAsStateWithLifecycle(false)

    LaunchedEffect(startScreen.value) {
        startScreen.value.let {
            navController.navigate(it) {
                popUpTo(Screen.Home) { inclusive = false }
            }
            startScreen.value = Screen.Home
        }
    }
    Scaffold(
        bottomBar = {
            val currentEntry by navController.currentBackStackEntryAsState()
            if (currentEntry?.destination?.hasRoute<Screen.Run>() == false && currentEntry?.destination?.hasRoute<Screen.Dashboard>() == false) {
                BottomNavigationBar(navController = navController, isRunActive = isRunActive)
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