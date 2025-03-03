package kolomyichuk.runly.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.profile),
            menuIcon = Icons.Outlined.Settings,
            onMenuClick = onNavigateToSettings
        )
        ContentProfileScreen()
    }

}

@Composable
fun ContentProfileScreen() {
    val username = "@username"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                imageUri = uri
            }
        )


        UserProfilePicture(
            onEditClick = {
                galleryLauncher.launch("image/*")
            },
            imageUrl = imageUri?.toString()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = username,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
    }
}

@Composable
fun UserProfilePicture(
    imageUrl: String?,
    onEditClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.size(120.dp)
    ) {
        Image(
            painter = if (imageUrl != null) {
                rememberAsyncImagePainter(model = imageUrl)
            } else {
                painterResource(R.drawable.user)
            },
            contentDescription = "User profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)

        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .offset(x = 6.dp, y = 6.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                .clickable {
                    onEditClick()
                }
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}