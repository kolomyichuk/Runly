package kolomyichuk.runly.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun ClickableLinkText(
    text: String,
    url: String
) {
    val context = LocalContext.current

    Text(
        text = text,
        fontSize = 10.sp,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.tertiary
    )
}
