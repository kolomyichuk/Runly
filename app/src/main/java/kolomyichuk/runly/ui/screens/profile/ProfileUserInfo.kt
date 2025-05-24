package kolomyichuk.runly.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileUserInfo(
    username: String,
    imageFilePath: String?,
    onSaveUsername: (String) -> Unit,
    onPickImage: (Uri) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onPickImage(it) }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileUserImage(
            imageFilePath = imageFilePath,
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
            ProfileAddUserName(
                newName = newName,
                onNameChange = { newName = it },
                onConfirm = {
                    if (newName.isNotBlank()) {
                        onSaveUsername(newName)
                        showDialog = false
                    }
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}