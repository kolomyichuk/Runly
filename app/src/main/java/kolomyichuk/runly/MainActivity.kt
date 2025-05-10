package kolomyichuk.runly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.MainScreen
import kolomyichuk.runly.ui.navigation.Screen
import kolomyichuk.runly.ui.screens.theme.ThemeViewModel
import kolomyichuk.runly.ui.theme.RunlyTheme

const val ACTION_SHOW_RUN_SCREEN = "ACTION_SHOW_RUN_SCREEN"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val currentScreen = mutableStateOf<Screen>(Screen.Home)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val currentTheme by themeViewModel.themeFlow.collectAsState(initial = AppTheme.SYSTEM)

            val isDarkTheme = when (currentTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            RunlyTheme(darkTheme = isDarkTheme) {
                MainScreen(currentScreen.value, onCurrentScreenChange = {
                    currentScreen.value = Screen.Home
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == ACTION_SHOW_RUN_SCREEN) {
            currentScreen.value = Screen.Run
        }
    }
}

