package kolomyichuk.runly.ui.screens.run

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.run),
            onBackClick = { navController.popBackStack() }
        )
        RunScreenContent(
            navController = navController,
            runViewModel = runViewModel
        )
    }
}
