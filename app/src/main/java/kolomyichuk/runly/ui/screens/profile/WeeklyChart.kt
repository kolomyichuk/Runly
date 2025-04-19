package kolomyichuk.runly.ui.screens.profile

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun WeeklyRunLineChart(
    weeklyDistances: List<Float>,
    modifier: Modifier = Modifier
) {
    val weeks = listOf("4", "3", "2", "1", "0")

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setScaleEnabled(false)
                setDrawGridBackground(false)
                setPinchZoom(true)

                val topLimit = LimitLine(
                    (weeklyDistances.maxOrNull() ?: 0f),
                    "Top"
                ).apply {
                    lineColor = Color.GRAY
                    lineWidth = 1f
                    disableDashedLine()
                    label = ""
                }

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    valueFormatter = IndexAxisValueFormatter(weeks)
                    granularity = 1f
                    setDrawLabels(false)
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(true)
                    textSize = 12f
                    addLimitLine(topLimit)
                }

                axisRight.apply {
                    isEnabled = true
                    setDrawAxisLine(true)
                    setDrawGridLines(false)
                    setDrawLabels(false)
                }


                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = weeklyDistances.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }

            val dataSet = LineDataSet(entries, "Distance").apply {
                color = ColorTemplate.COLORFUL_COLORS[3]
                setDrawCircles(true)
                setCircleColor(ColorTemplate.COLORFUL_COLORS[0])
                lineWidth = 2f
                circleRadius = 5f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}
