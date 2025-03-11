package kolomyichuk.runly.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun StartMarker(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier.size(22.dp)) {
        val radius = size.minDimension / 2
        drawCircle(
            color = primaryColor,
            radius = radius * 0.8f,
            center = center
        )
        drawCircle(
            color = Color.White,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun CurrentLocationMarker(modifier: Modifier = Modifier) {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    Canvas(
        modifier = modifier.size(22.dp)
    ) {
        val radius = size.minDimension / 2
        drawCircle(
            color = tertiaryColor,
            radius = radius * 0.8f,
            center = center
        )
        drawCircle(
            color = Color.White,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun FinishMarker(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(22.dp)) {
        val radius = size.minDimension / 2
        val tileSize = radius * 0.8f
        for (i in 0..2) {
            for (j in 0..2) {
                drawRect(
                    color = if ((i + j) % 2 == 0) Color.Black else Color.White,
                    topLeft = Offset(
                        center.x - tileSize + i * tileSize / 1.5f,
                        center.y - tileSize + j * tileSize / 1.5f
                    ),
                    size = Size(tileSize / 1.5f, tileSize / 1.5f)
                )
            }
        }
        drawCircle(
            color = Color.White,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}