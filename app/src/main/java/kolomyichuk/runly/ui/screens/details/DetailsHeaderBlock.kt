package kolomyichuk.runly.ui.screens.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R

@Composable
fun DetailsHeaderBlock(
    timestamp: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.date),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = timestamp,
            fontSize = 14.sp
        )
    }
}