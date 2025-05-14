package kolomyichuk.runly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.screens.main.MainScreen
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.screens.theme.ThemeViewModel
import kolomyichuk.runly.ui.theme.RunlyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRootScreen()
    }

    private fun initRootScreen(initScreen: Screen = Screen.Home) {
        setContent {
            val navController = rememberNavController()
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val currentTheme by themeViewModel.themeFlow.collectAsState(initial = AppTheme.SYSTEM)

            LaunchedEffect(initScreen) {
                navController.navigate(initScreen)
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
                MainScreen(navController)
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

