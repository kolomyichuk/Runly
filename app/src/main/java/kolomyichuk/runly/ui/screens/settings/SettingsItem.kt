package kolomyichuk.runly.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.ui.components.CircleIconButton

@Composable
fun SettingsItem(
    label: String,
    onNavigateToSettingsItem: () -> Unit,
    settingsItemIcon: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToSettingsItem() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(
            iconResId = settingsItemIcon,
            contentDescription = "",
            iconColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            buttonSize = 40.dp
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null
        )
    }
}