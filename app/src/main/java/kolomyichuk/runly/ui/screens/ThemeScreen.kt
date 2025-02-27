package kolomyichuk.runly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.data.repository.AppTheme
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.viewmodel.ThemeViewModel


@Composable
fun ThemeScreen(
    onBack:()->Unit,
    viewModel:ThemeViewModel,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(title = "Theme")
        ContentThemeScreen(viewModel = viewModel)
    }
}

@Composable
fun ContentThemeScreen(
    viewModel:ThemeViewModel
){
    val currentTheme by viewModel.themeFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Choice Theme",
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

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
        Spacer(modifier = Modifier.width(8.dp))
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
                unselectedColor = MaterialTheme.colorScheme.onBackground   //?
            )
        )
    }
}


@Composable
fun ThemeIcon(
    theme: AppTheme
) {
    val icon = when (theme) {
        AppTheme.LIGHT -> Icons.Default.WbSunny
        AppTheme.DARK -> Icons.Default.NightsStay
        AppTheme.SYSTEM -> Icons.Default.Settings
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(40.dp),
        tint = MaterialTheme.colorScheme.onBackground
    )
}


fun themeName(theme: AppTheme): String {
    return when (theme) {
        AppTheme.LIGHT -> "Light mode"
        AppTheme.DARK -> "Dark mode"
        AppTheme.SYSTEM -> "Phone default"
    }
}


