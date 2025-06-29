package kolomyichuk.runly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.data.model.AppTheme
import kolomyichuk.runly.ui.navigation.AuthNavGraph
import kolomyichuk.runly.ui.navigation.HomeNavGraph
import kolomyichuk.runly.ui.navigation.RunNavGraph
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.screens.main.MainScreen
import kolomyichuk.runly.ui.screens.main.MainViewModel
import kolomyichuk.runly.ui.theme.RunlyTheme

val LocalAppTheme = compositionLocalOf { AppTheme.SYSTEM }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRootScreen()
    }

    private fun initRootScreen(initScreen: Screen = Screen.Home) {
        setContent {
            val navController = rememberNavController()
            val mainViewModel: MainViewModel = hiltViewModel()
            val currentTheme by mainViewModel.themeState.collectAsState(initial = AppTheme.SYSTEM)

            val isUserSignIn by mainViewModel.isUserSignIn.collectAsStateWithLifecycle()
            val startDestination = if (isUserSignIn) {
                when (initScreen) {
                    Screen.Home -> HomeNavGraph
                    Screen.Run -> RunNavGraph
                    else -> HomeNavGraph
                }
            } else {
                AuthNavGraph
            }

            LaunchedEffect(initScreen) {
                if (isUserSignIn) {
                    navController.navigate(initScreen)
                }
            }

            DisposableEffect(Unit) {
                val listener = Consumer<Intent> {
                    if (it.extras?.getString(EXTRA_SCREEN) == SCREEN_RUN) {
                        navController.navigate(Screen.Run)
                    }
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }

            val isDarkTheme = when (currentTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            RunlyTheme(darkTheme = isDarkTheme) {
                CompositionLocalProvider(LocalAppTheme provides currentTheme) {
                    MainScreen(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.extras?.getString(EXTRA_SCREEN) == SCREEN_RUN) {
            initRootScreen(initScreen = Screen.Run)
        }
    }

    companion object {
        const val EXTRA_SCREEN = "EXTRA_SCREEN"
        const val SCREEN_RUN = "run"
    }
}

