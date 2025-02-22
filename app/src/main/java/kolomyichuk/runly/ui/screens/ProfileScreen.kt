package kolomyichuk.runly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R

@Composable
fun ProfileScreen() {
    val username = "@username"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(top = 20.dp),
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "User Image"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}