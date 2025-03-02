@file:JvmName("ButtonKt")

package kolomyichuk.runly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector? = null,
    iconResId: Int? = null,
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
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.size(iconSize)
                )
            } else if (iconResId != null) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier
                        .size(iconSize)
                        .background(backgroundColor)
                )
            }
        }
    }
}


@Composable
fun ButtonStart(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    roundDp: Dp = 8.dp,
) {
    Box(modifier = modifier) {
        Button(
            shape = RoundedCornerShape(roundDp),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )

        ) {
            Text(
                text = text
            )
        }
    }
}
