package kolomyichuk.runly.ui.screens.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.TopBarApp


@Composable
fun ThemeScreen(
    onBack: () -> Unit,
    viewModel: ThemeViewModel,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.theme),
            onBackClick = onBack
        )
        ThemeScreenContent(viewModel = viewModel)
    }
}

@Composable
private fun ThemeScreenContent(
    viewModel: ThemeViewModel
) {
    val currentTheme by viewModel.themeState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppTheme.entries.forEach { theme ->
                ThemeOption(theme, currentTheme) {viewModel.saveTheme(it)}
            }
        }
    }
}


