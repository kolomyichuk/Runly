package kolomyichuk.runly.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.HomeScreen
import kolomyichuk.runly.ui.screens.RunScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen() }
            composable("run") { RunScreen()  }
            composable("profile") { ProfileNavHost() }
        }
    }
}
