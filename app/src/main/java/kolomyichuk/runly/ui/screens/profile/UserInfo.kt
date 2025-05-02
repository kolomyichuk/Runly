package kolomyichuk.runly.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.viewmodel.ProfileViewModel

@Composable
fun UserInfo(
    profileViewModel: ProfileViewModel
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val username by profileViewModel.username.collectAsState()
    var newName by remember { mutableStateOf("") }
    val imageFile by profileViewModel.imageFilePath.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            profileViewModel.saveProfileImage(uri)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserImage(
            imageFilePath = imageFile,
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
                                profileViewModel.saveUsername(newName)
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