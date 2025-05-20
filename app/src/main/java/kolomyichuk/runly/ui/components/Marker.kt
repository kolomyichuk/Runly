package kolomyichuk.runly.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

val currentLocationMarker = canvasToBitmapDescriptor(50) { canvas, paint ->
    val centerX = 25f
    val centerY = 25f
    val outerRadius = 20f
    val strokeWidth = 4f
    val innerRadius = outerRadius - strokeWidth / 2

    paint.style = Paint.Style.STROKE
    paint.color = Color.White.toArgb()
    paint.strokeWidth = strokeWidth
    canvas.drawCircle(centerX, centerY, outerRadius, paint)

    paint.style = Paint.Style.FILL
    paint.color = Color.Blue.toArgb()
    canvas.drawCircle(centerX, centerY, innerRadius, paint)
}

// TODO Do we need it? Or it will be used in the future?
@Suppress("NAME_SHADOWING")
val finishMarker = canvasToBitmapDescriptor(50) { canvas, paint ->
    val centerX = 25f
    val centerY = 25f
    val radius = 20f
    val strokeWidth = 4f
    val tileSize = radius - strokeWidth / 2
    val paintBlack = Paint().apply { color = Color.Black.toArgb(); style = Paint.Style.FILL }
    val paintWhite = Paint().apply { color = Color.White.toArgb(); style = Paint.Style.FILL }

    val path = Path().apply { addCircle(centerX, centerY, radius, Path.Direction.CCW) }
    canvas.save()
    canvas.clipPath(path)

    for (i in 0..2) {
        for (j in 0..2) {
            val paint = if ((i + j) % 2 == 0) paintBlack else paintWhite
            val left = centerX - tileSize + i * tileSize / 1.5f
            val top = centerY - tileSize + j * tileSize / 1.5f
            val size = tileSize / 1.5f
            canvas.drawRect(left, top, left + size, top + size, paint)
        }
    }

    canvas.restore()

    paint.style = Paint.Style.STROKE
    paint.color = Color.White.toArgb()
    paint.strokeWidth = 4f
    canvas.drawCircle(centerX, centerY, radius, paint)
}

// TODO Do we need it? Or it will be used in the future?
val startMarker = canvasToBitmapDescriptor(50) { canvas, paint ->
    val centerX = 25f
    val centerY = 25f
    val outerRadius = 20f
    val strokeWidth = 4f
    val innerRadius = outerRadius - strokeWidth / 2

    paint.style = Paint.Style.STROKE
    paint.color = android.graphics.Color.WHITE
    paint.strokeWidth = strokeWidth
    canvas.drawCircle(centerX, centerY, outerRadius, paint)

    paint.style = Paint.Style.FILL
    paint.color = Color.Green.toArgb()
    canvas.drawCircle(centerX, centerY, innerRadius, paint)
}

fun canvasToBitmapDescriptor(size: Int, draw: (Canvas, Paint) -> Unit): BitmapDescriptor {
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    draw(canvas, paint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}









