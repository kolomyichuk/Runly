package kolomyichuk.runly.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kolomyichuk.runly.ui.screens.signin.SignInScreen
import kolomyichuk.runly.ui.screens.signin.SignInViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthNavGraph>(startDestination = Screen.SignIn) {
        composable<Screen.SignIn> {
            val signInViewModel: SignInViewModel = hiltViewModel()
            val coroutineScope = rememberCoroutineScope()
            SignInScreen(
                onSignInSuccess = { idToken ->
                    coroutineScope.launch {
                        signInViewModel.signInWithGoogle(idToken)
                        navController.navigate(HomeNavGraph) {
                            popUpTo<Screen.SignIn> { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
