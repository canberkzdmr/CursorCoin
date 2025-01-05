package com.cursorcoin.presentation.screens.market

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cursorcoin.presentation.components.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketAnalysisScreen(
    viewModel: MarketAnalysisViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Market Analysis") }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.marketData?.let { marketData ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Market Overview Section
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Market Overview",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Active Cryptocurrencies",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${marketData.activeCryptocurrencies}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Active Markets",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${marketData.markets}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Market Metrics
                    MarketMetricCard(
                        title = "Total Market Cap",
                        value = formatCurrency(marketData.totalMarketCap),
                        change = marketData.marketCapPercentageChange24h
                    )

                    MarketMetricCard(
                        title = "24h Volume",
                        value = formatCurrency(marketData.totalVolume)
                    )

                    // Market Dominance Chart
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Market Dominance",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Create pie chart data
                            val entries = marketData.topCoinsDominance
                                .filter { it.value >= 1.0 } // Show only coins with >1% dominance
                                .map { (symbol, percentage) ->
                                    PieEntry(percentage.toFloat(), symbol.uppercase())
                                }
                            
                            val dataSet = PieDataSet(entries, "Market Dominance").apply {
                                colors = listOf(
                                    android.graphics.Color.parseColor("#FF9500"),
                                    android.graphics.Color.parseColor("#2196F3"),
                                    android.graphics.Color.parseColor("#4CAF50"),
                                    android.graphics.Color.parseColor("#9C27B0"),
                                    android.graphics.Color.parseColor("#F44336")
                                )
                                valueTextSize = 12f
                                valueFormatter = PercentFormatter()
                            }

                            PieChart(
                                pieData = PieData(dataSet),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarketMetricCard(
    title: String,
    value: String,
    change: Double? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                change?.let {
                    val color = if (it >= 0) Color(0xFF4CAF50) else Color(0xFFE53935)
                    val prefix = if (it >= 0) "+" else ""
                    Text(
                        text = "$prefix${String.format("%.2f", it)}%",
                        color = color,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}

class PercentFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.1f%%", value)
    }
} 