package kolomyichuk.runly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kolomyichuk.runly.ui.screens.RunScreen
import kolomyichuk.runly.ui.theme.RunlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunlyTheme {
                RunScreen()
            }
        }
    }
}

