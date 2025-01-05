package com.cursorcoin.presentation.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData

@Composable
fun PieChart(
    pieData: PieData,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.TRANSPARENT)
                holeRadius = 58f
                transparentCircleRadius = 61f
                setDrawCenterText(false)
                isRotationEnabled = true
                isHighlightPerTapEnabled = true
                legend.isEnabled = true
                legend.textSize = 12f
                legend.textColor = Color.GRAY
                setEntryLabelColor(Color.WHITE)
                setEntryLabelTextSize(11f)
            }
        },
        update = { chart ->
            chart.data = pieData
            chart.invalidate()
        }
    )
} 