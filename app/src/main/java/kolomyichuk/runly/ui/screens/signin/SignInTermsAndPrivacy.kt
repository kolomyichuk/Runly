package kolomyichuk.runly.ui.screens.signin

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R

@Composable
fun SignInTermsAndPrivacy() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.by_continuing_you_are_agreeing_to_our),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row {
            Text(
                text = stringResource(R.string.terms_of_service),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/termsofserviceforrunly")
                    )
                    context.startActivity(intent)
                }
            )

            Text(
                text = stringResource(R.string.and),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.privacy_policy),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/runly-privacy-policy")
                    )
                    context.startActivity(intent)
                }
            )
        }
    }
}
