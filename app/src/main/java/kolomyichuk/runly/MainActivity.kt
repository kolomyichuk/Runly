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
import kolomyichuk.runly.ui.theme.RunlyTheme
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel
import kolomyichuk.runly.utils.Constants

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
            val startScreen = remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                if (intent?.action == Constants.ACTION_SHOW_RUN_SCREEN){
                    startScreen.value = "run"
                }
            }

            RunlyTheme(darkTheme = isDarkTheme) {
                MainScreen(navController, startScreen)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Constants.ACTION_SHOW_RUN_SCREEN){
            startActivity(Intent(this, MainActivity::class.java).apply {
                action = Constants.ACTION_SHOW_RUN_SCREEN
            })
        }
    }
}

