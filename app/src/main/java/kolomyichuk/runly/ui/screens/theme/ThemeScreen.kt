package kolomyichuk.runly.ui.screens.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.settings.model.AppTheme
import kolomyichuk.runly.ui.components.TopBarApp


@Composable
fun ThemeScreen(
    onBack: () -> Unit,
    viewModel: ThemeViewModel,
) {
    Scaffold(
        topBar = {
            TopBarApp(
                title = stringResource(R.string.theme),
                onBackClick = onBack
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ThemeScreenContent(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun ThemeScreenContent(
    viewModel: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.themeState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppTheme.entries.forEach { theme ->
                ThemeOption(theme, currentTheme) { viewModel.saveTheme(it) }
            }
        }
    }
}
