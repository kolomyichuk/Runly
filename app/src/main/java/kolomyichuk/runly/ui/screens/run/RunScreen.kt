package kolomyichuk.runly.ui.screens.run

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kolomyichuk.runly.LocalAppTheme
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.TopBarApp

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RunScreen(
    navController: NavController,
    runViewModel: RunViewModel = hiltViewModel(),
) {
    val appTheme = LocalAppTheme.current
    val isDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.run),
            onBackClick = { navController.popBackStack() }
        )
        RunScreenContent(
            isDarkTheme = isDarkTheme,
            navController = navController,
            runViewModel = runViewModel
        )
    }
}
