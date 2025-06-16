package kolomyichuk.runly.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kolomyichuk.runly.ui.screens.signin.SignInScreen
import kolomyichuk.runly.ui.screens.signin.SignInViewModel

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthNavGraph>(startDestination = Screen.SignIn) {
        composable<Screen.SignIn> {
            val signInViewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                signInViewModel = signInViewModel,
                onSignInSuccess = {
                    navController.navigate(HomeNavGraph) {
                        popUpTo<Screen.SignIn> { inclusive = true }
                    }
                }
            )
        }
    }
}