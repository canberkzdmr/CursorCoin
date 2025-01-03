package com.cursorcoin.presentation.components

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LineChart(
    data: List<Pair<Long, Float>>,
    modifier: Modifier = Modifier
) {
    val entries = remember(data) {
        data.map { (timestamp, value) ->
            Entry(timestamp.toFloat(), value)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                setScaleEnabled(false)
                setPinchZoom(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                        private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

                        override fun getFormattedValue(value: Float): String =
                            dateFormat.format(Date(value.toLong()))
                    }
                    textColor = Color.GRAY
                    setDrawGridLines(false)
                }

                axisLeft.apply {
                    textColor = Color.GRAY
                    setDrawGridLines(true)
                    gridColor = Color.LTGRAY
                }

                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, "Price").apply {
                color = Color.BLUE
                setDrawCircles(false)
                setDrawValues(false)
                lineWidth = 2f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
} 