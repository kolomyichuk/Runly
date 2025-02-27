package kolomyichuk.runly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kolomyichuk.runly.data.repository.AppTheme
import kolomyichuk.runly.navigation.MainScreen
import kolomyichuk.runly.ui.theme.RunlyTheme
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:ThemeViewModel = hiltViewModel()
            val currentTheme by viewModel.themeFlow.collectAsState(initial = AppTheme.SYSTEM)

            val isDarkTheme = when(currentTheme){
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }
            RunlyTheme(darkTheme = isDarkTheme) {
                MainScreen()
            }
        }
    }
}

