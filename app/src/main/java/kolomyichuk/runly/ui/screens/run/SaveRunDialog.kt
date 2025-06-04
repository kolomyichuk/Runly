package kolomyichuk.runly.ui.screens.run

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kolomyichuk.runly.R
import kolomyichuk.runly.service.RunTrackingService

@Composable
fun SaveRunDialog(
    distance: String,
    onSaveRun: () -> Unit,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            sendCommandToRunService(
                context = context, route = RunTrackingService.ACTION_RESUME_TRACKING
            )
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                distance.toDoubleOrNull()?.let { value ->
                    if (value > 0) {
                        onSaveRun()
                        Toast.makeText(
                            context,
                            context.getString(R.string.run_saved), Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_run_not_saved), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                sendCommandToRunService(
                    context = context, route = RunTrackingService.ACTION_STOP_TRACKING
                )
                onDismiss()
                navController.popBackStack()
            }) {
                Text(text = stringResource(R.string.finish))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                sendCommandToRunService(
                    context = context, route = RunTrackingService.ACTION_RESUME_TRACKING
                )
                Text(text = stringResource(R.string.resume_run))
            }
        },
        title = { Text(text = stringResource(R.string.finish_the_run)) },
        text = { Text(text = stringResource(R.string.do_you_want_to_finish)) }
    )
}