package kolomyichuk.runly.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.ui.components.BottomNavigationBar
import kolomyichuk.runly.ui.screens.HomeScreen
import kolomyichuk.runly.ui.screens.RunScreen
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@Composable
fun MainScreen(navController: NavHostController, startScreen:MutableState<String?>) {
    LaunchedEffect(startScreen.value) {
        startScreen.value?.let {
            navController.navigate(it){
                popUpTo("home") {inclusive = false}
            }
            startScreen.value = null
        }
    }
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
