package kolomyichuk.runly.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kolomyichuk.runly.ui.screens.signin.SignInScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthNavGraph>(startDestination = Screen.SignIn) {
        composable<Screen.SignIn> {
            SignInScreen(navController = navController)
        }
    }
}