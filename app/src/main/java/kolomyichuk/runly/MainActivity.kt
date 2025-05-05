package kolomyichuk.runly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.navigation.MainScreen
import kolomyichuk.runly.navigation.Screen
import kolomyichuk.runly.ui.theme.RunlyTheme
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

const val ACTION_SHOW_RUN_SCREEN = "ACTION_SHOW_RUN_SCREEN"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

            val navController = rememberNavController()
            // TODO Let's try to get rid of nullable String in this State
            // TODO Make it non-nullable and pass some default value (default start screen)
            val startScreen = remember { mutableStateOf<Screen>(Screen.Home) }

            LaunchedEffect(Unit) {
                if (intent?.action == ACTION_SHOW_RUN_SCREEN) {
                    startScreen.value = Screen.Run
                }
            }

            RunlyTheme(darkTheme = isDarkTheme) {
                MainScreen(navController, startScreen)
            }
        }
    }

    // TODO This callback would not be invoked because a launch mode of the MainActivity is standard
    // TODO Let's talk about and learn Android Launch modes and get back to it
    // TODO More info:
    // https://medium.com/huawei-developers/android-launch-modes-explained-8b32ae01f204
    // https://www.youtube.com/watch?v=Z0AzoFOiH9c
    // https://developer.android.com/guide/components/activities/tasks-and-back-stack#ManifestForTasks
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == ACTION_SHOW_RUN_SCREEN) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                action = ACTION_SHOW_RUN_SCREEN
            })
        }
    }
}

