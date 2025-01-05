package com.cursorcoin.presentation.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData

@Composable
fun LineChart(
    lineData: LineData,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                description.isEnabled = false
                legend.isEnabled = false
                
                axisRight.isEnabled = false
                axisLeft.apply {
                    textColor = Color.GRAY
                    setDrawGridLines(false)
                }
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.GRAY
                    setDrawGridLines(false)
                    setDrawLabels(false)
                }

                setNoDataText("No price history available")
                setNoDataTextColor(Color.GRAY)
                
                setDrawGridBackground(false)
                setDrawBorders(false)
            }
        },
        update = { chart ->
            chart.data = lineData
            chart.invalidate()
        }
    )
} 