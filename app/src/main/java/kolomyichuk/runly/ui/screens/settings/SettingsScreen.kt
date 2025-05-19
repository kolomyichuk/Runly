package kolomyichuk.runly.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.CircleIconButton
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToTheme: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.settings),
            onBackClick = onBack
        )
        SettingsScreenContent(onNavigateToTheme = onNavigateToTheme)
    }
}

@Composable
private fun SettingsScreenContent(
    onNavigateToTheme: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTheme() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleIconButton(
                    iconResId = R.drawable.brush,
                    contentDescription = "",
                    iconColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    buttonSize = 40.dp
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.theme),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null
                )
            }
            Spacer(
                Modifier
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
        }
    }
}