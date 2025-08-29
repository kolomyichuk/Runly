package kolomyichuk.runly.ui.screens.run

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RunScreen(
    navController: NavController,
    runViewModel: RunViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.run),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        RunScreenContent(
            navController = navController,
            runViewModel = runViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
