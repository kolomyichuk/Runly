package kolomyichuk.runly.ui.screens.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kolomyichuk.runly.R

@Composable
fun ProfileAddUserName(
    newName: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_username)) },
        text = {
            TextField(
                value = newName,
                onValueChange = onNameChange,
                label = { Text(text = stringResource(R.string.enter_your_username)) }
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newName.isNotEmpty(),
            ) {
                Text(text = stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
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