package kolomyichuk.runly.ui.screens.theme

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kolomyichuk.runly.R
import kolomyichuk.runly.data.local.datastore.AppTheme
import kolomyichuk.runly.ui.components.CircleIconButton

@Composable
fun ThemeOption(
    theme: AppTheme,
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    val context = LocalContext.current
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
            text = theme.getThemeName(context = context),
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
private fun ThemeIcon(
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

private fun AppTheme.getThemeName(context:Context): String {
    return when (this) {
        AppTheme.SYSTEM -> context.getString(R.string.system)
        AppTheme.DARK -> context.getString(R.string.dark)
        AppTheme.LIGHT -> context.getString(R.string.light)
    }
}