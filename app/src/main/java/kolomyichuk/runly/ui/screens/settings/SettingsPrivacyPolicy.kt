package kolomyichuk.runly.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.ClickableLinkText

@Composable
fun SettingsPrivacyPolicy() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ClickableLinkText(
            text = stringResource(R.string.privacy_policy),
            url = "https://sites.google.com/view/runly-privacy-policy"
        )
    }
}
