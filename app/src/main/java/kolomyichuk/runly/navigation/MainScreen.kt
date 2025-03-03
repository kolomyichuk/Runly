package kolomyichuk.runly.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.HomeScreen
import kolomyichuk.runly.ui.screens.RunScreen
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != "run") {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen() }

            composable("run") {
                val themeViewModel = hiltViewModel<ThemeViewModel>()
                RunScreen(
                    navController = navController,
                    themeViewModel = themeViewModel
                )
            }

            composable("profile") { ProfileNavHost() }
        }
    }
}
