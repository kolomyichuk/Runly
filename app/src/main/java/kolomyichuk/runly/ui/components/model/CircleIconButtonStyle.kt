package kolomyichuk.runly.ui.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CircleIconButtonStyle(
    val backgroundColor: Color = Color.White,
    val iconColor: Color = Color.Black,
    val buttonSize: Dp = 40.dp,
    val iconSize: Dp = 28.dp,
    val elevation: Dp = 8.dp,
)
