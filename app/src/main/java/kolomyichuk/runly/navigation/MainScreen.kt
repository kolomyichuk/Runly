package kolomyichuk.runly.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.home.HomeScreen

@Composable
fun MainScreen(navController: NavHostController, startScreen: MutableState<String?>) {
    val isRunActive by RunTrackingService.isActiveRun.collectAsState(false)

    LaunchedEffect(startScreen.value) {
        startScreen.value?.let {
            navController.navigate(it) {
                popUpTo("home") { inclusive = false }
            }
            startScreen.value = null
        }
    }
    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != "run") {
                BottomNavigationBar(navController = navController, isRunActive = isRunActive)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen() }
            composable("run") { RunNavHost() }
            composable("profile") { ProfileNavHost() }
        }
    }
}
