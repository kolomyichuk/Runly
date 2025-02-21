@file:JvmName("ButtonKt")

package kolomyichuk.runly.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    backgroundColor: Color = Color.White,
    iconColor: Color = Color.Black,
    buttonSize: Dp = 40.dp,
    iconSize: Dp = 28.dp,
    elevation: Dp = 8.dp,
) {
    Surface(
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = elevation,
        modifier = Modifier.size(buttonSize),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()

        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}