package kolomyichuk.runly.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.navigation.profileNavGraph
import kolomyichuk.runly.ui.navigation.runNavGraph
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.home.HomeScreen
import kolomyichuk.runly.ui.screens.home.HomeViewModel

@Composable
fun MainScreen(
    currentScreen: Screen,
    onCurrentScreenChange: () -> Unit
) {
    val navController = rememberNavController()

    LaunchedEffect(currentScreen) {
        currentScreen.let {
            navController.navigate(it) {
                popUpTo(Screen.Home) { inclusive = false }
            }
            onCurrentScreenChange()
        }
    }

    Scaffold(
        bottomBar = {
            val currentEntry by navController.currentBackStackEntryAsState()
            if (currentEntry?.destination?.hasRoute<Screen.Run>() == false &&
                currentEntry?.destination?.hasRoute<Screen.Dashboard>() == false
            ) {
                BottomNavigationBar(navController)
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