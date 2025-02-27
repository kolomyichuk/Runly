package kolomyichuk.runly.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = "Home"
        )
        ContentHomeScreen()
    }

}

@Composable
fun ContentHomeScreen(
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Home Screen Content")
    }
}
