package kolomyichuk.runly.ui.screens.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.ClickableLinkText

private const val TERMS_OF_SERVICE = "https://sites.google.com/view/termsofserviceforrunly"
const val PRIVACY_POLICY = "https://sites.google.com/view/runly-privacy-policy"

@Composable
fun SignInTermsAndPrivacy() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.by_continuing_you_are_agreeing_to_our),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row {
            ClickableLinkText(
                text = stringResource(R.string.terms_of_service),
                url = TERMS_OF_SERVICE
            )

            Text(
                text = stringResource(R.string.and),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            ClickableLinkText(
                text = stringResource(R.string.privacy_policy_sign_in),
                url = PRIVACY_POLICY
            )
        }
    }
}
