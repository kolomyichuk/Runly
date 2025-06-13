package kolomyichuk.runly.ui.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kolomyichuk.runly.ui.navigation.Screen

@Composable
fun SignInScreen(
    navController: NavController
) {
    Scaffold { innerPadding ->
        SignInScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController
        )
    }
}


@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            navController.navigate(Screen.Main)
        }) {
            Text(text = "Sign In")
        }
    }
}