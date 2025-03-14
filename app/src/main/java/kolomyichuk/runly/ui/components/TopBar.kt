package kolomyichuk.runly.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarApp(
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    menuIcon: ImageVector? = null,
    onMenuClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            title?.let { text ->
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        navigationIcon = {
            onBackClick?.let {
                IconButton(
                    onClick = it
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            menuIcon?.let {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = menuIcon,
                        contentDescription = null
                    )
                }
            }
        },
    )
}