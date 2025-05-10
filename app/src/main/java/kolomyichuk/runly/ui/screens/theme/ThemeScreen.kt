package kolomyichuk.runly.ui.screens.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.CircleIconButton
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
        ContentThemeScreen(viewModel = viewModel)
    }
}

@Composable
fun ContentThemeScreen(
    viewModel: ThemeViewModel
) {
    val currentTheme by viewModel.themeFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ThemeOption(AppTheme.LIGHT, currentTheme) { viewModel.saveTheme(it) }
            ThemeOption(AppTheme.DARK, currentTheme) { viewModel.saveTheme(it) }
            ThemeOption(AppTheme.SYSTEM, currentTheme) { viewModel.saveTheme(it) }
        }
    }
}


@Composable
fun ThemeOption(
    theme: AppTheme,
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onThemeSelected(theme) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeIcon(theme = theme)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = themeName(theme),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground
        )
        RadioButton(
            selected = theme == selectedTheme,
            onClick = { onThemeSelected(theme) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}


@Composable
fun ThemeIcon(
    theme: AppTheme
) {
    val icon = when (theme) {
        AppTheme.LIGHT -> Icons.Outlined.LightMode
        AppTheme.DARK -> Icons.Outlined.ModeNight
        AppTheme.SYSTEM -> Icons.Outlined.Settings
    }
    CircleIconButton(
        imageVector = icon,
        contentDescription = "",
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        iconColor = MaterialTheme.colorScheme.primary,
        buttonSize = 40.dp
    )
}

@Composable
fun themeName(theme: AppTheme): String {
    return when (theme) {
        AppTheme.LIGHT -> stringResource(R.string.light)
        AppTheme.DARK -> stringResource(R.string.dark)
        AppTheme.SYSTEM -> stringResource(R.string.system)
    }
}


