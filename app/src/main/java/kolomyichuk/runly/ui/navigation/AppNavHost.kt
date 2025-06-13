package kolomyichuk.runly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kolomyichuk.runly.ui.screens.main.MainScreen
import kolomyichuk.runly.ui.screens.main.MainViewModel
import kolomyichuk.runly.ui.screens.signin.SignInScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn
    ) {
        composable<Screen.SignIn> {
            SignInScreen(navController = navController)
        }
        composable<Screen.Main> {
            MainScreen(
                mainViewModel = mainViewModel
            )
        }
    }
}