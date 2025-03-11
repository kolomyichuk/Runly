package kolomyichuk.runly.ui.screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp
import kolomyichuk.runly.ui.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.profile),
            menuIcon = Icons.Outlined.Settings,
            onMenuClick = onNavigateToSettings
        )
        ContentProfileScreen(
            viewModel = viewModel
        )
    }

}

@Composable
fun ContentProfileScreen(
    viewModel: ProfileViewModel
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val username by viewModel.username.collectAsState()
    var newName by remember { mutableStateOf("") }
    val imageFile by viewModel.imageFile.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.saveProfileImage(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserProfileImage(
            imageFile = imageFile,
            onEditClick = { launcher.launch("image/*") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { showDialog = true }
        ) {
            Text(
                text = username,
                fontSize = 18.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Add username") },
                text = {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text(text = stringResource(R.string.enter_your_username)) }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                viewModel.saveUsername(newName)
                                showDialog = false
                            }
                        },
                        enabled = newName.isNotEmpty(),
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
fun UserProfileImage(
    imageFile: File?,
    onEditClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.size(120.dp)
    ) {
        if (imageFile != null) {
            AsyncImage(
                model = imageFile.absolutePath,
                contentDescription = "User picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = "User profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)

            )
        }
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