@file:JvmName("ButtonKt")

package kolomyichuk.runly.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kolomyichuk.runly.R

@Composable
fun CircleIconButton(
    onClick: (() -> Unit)? = null,
    imageVector: ImageVector? = null,
    iconResId: Int? = null,
    contentDescription: String? = null,
    backgroundColor: Color = Color.White,
    iconColor: Color = Color.Black,
    buttonSize: Dp = 40.dp,
    iconSize: Dp = 28.dp,
    elevation: Dp = 8.dp,
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else Modifier

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = elevation,
        modifier = Modifier
            .size(buttonSize)
            .then(clickableModifier)
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
fun StartButton(
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

@Composable
fun StopButton(
    onClick: () -> Unit
) {
    CircleIconButton(
        onClick = onClick,
        imageVector = Icons.Filled.Stop,
        iconColor = MaterialTheme.colorScheme.onPrimary,
        elevation = 10.dp,
        iconSize = 28.dp,
        buttonSize = 40.dp,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentDescription = stringResource(R.string.stop)
    )
}

@Composable
fun MapVisibilityButton(
    onClick: () -> Unit
) {
    CircleIconButton(
        onClick = onClick,
        imageVector = Icons.Outlined.Map,
        iconColor = MaterialTheme.colorScheme.onPrimary,
        elevation = 10.dp,
        iconSize = 28.dp,
        buttonSize = 40.dp,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentDescription = null
    )
}

@Composable
fun MapTypeToggleButton(
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null
        )
    }
}

@Composable
fun GoogleSignInButton(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onSignInClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.sign_in_with_google),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

