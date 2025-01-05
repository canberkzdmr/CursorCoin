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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cursorcoin.domain.model.MarketData
import com.cursorcoin.domain.model.TopCoin
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
                    MarketOverviewSection(marketData)

                    // Market Metrics Section
                    MarketMetricsSection(marketData)

                    // Market Dominance Chart
                    MarketDominanceSection(marketData)

                    // DeFi Section
                    DefiSection(marketData)

                    // Currency Distribution Section
                    CurrencyDistributionSection(marketData)

                    // Top Cryptocurrencies Section
                    TopCryptocurrenciesSection(marketData)
                }
            }
        }
    }
}

@Composable
private fun MarketOverviewSection(marketData: MarketData) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
}

@Composable
private fun MarketMetricsSection(marketData: MarketData) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MarketMetricCard(
            title = "Total Market Cap",
            value = formatCurrency(marketData.totalMarketCap),
            change = marketData.marketCapPercentageChange24h
        )

        MarketMetricCard(
            title = "24h Volume",
            value = formatCurrency(marketData.totalVolume)
        )

        MarketMetricCard(
            title = "Bitcoin Dominance",
            value = "${String.format("%.2f", marketData.btcDominance)}%"
        )
    }
}

@Composable
private fun MarketDominanceSection(marketData: MarketData) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
            
            val entries = marketData.topCoinsDominance
                .filter { it.value >= 1.0 }
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

@Composable
private fun DefiSection(marketData: MarketData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "DeFi Overview",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "DeFi Market Cap",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatCurrency(marketData.defiMarketCap),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = "DeFi Dominance",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${String.format("%.2f", marketData.defiDominance)}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyDistributionSection(marketData: MarketData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Top Market Cap by Currency",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val topCurrencies = listOf("usd", "eur", "gbp", "jpy", "cny")
            val entries = marketData.marketCapByCurrency
                .filter { it.key in topCurrencies }
                .map { (currency, value) ->
                    PieEntry(value.toFloat(), currency.uppercase())
                }

            val dataSet = PieDataSet(entries, "Currency Distribution").apply {
                colors = listOf(
                    android.graphics.Color.parseColor("#4CAF50"),
                    android.graphics.Color.parseColor("#2196F3"),
                    android.graphics.Color.parseColor("#9C27B0"),
                    android.graphics.Color.parseColor("#FF9800"),
                    android.graphics.Color.parseColor("#F44336")
                )
                valueTextSize = 12f
                valueFormatter = CurrencyFormatter()
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

@Composable
private fun TopCryptocurrenciesSection(marketData: MarketData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Top Cryptocurrencies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Asset",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = "24h %",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Coin list
            marketData.topCoins.forEach { coin ->
                CoinListItem(coin)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun CoinListItem(coin: TopCoin) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Main row with basic info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = coin.image,
                    contentDescription = coin.name,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = coin.symbol.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Text(
                text = formatCurrency(coin.currentPrice),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            Text(
                text = formatPercentage(coin.percentageChange24h),
                style = MaterialTheme.typography.bodyMedium,
                color = getPercentageColor(coin.percentageChange24h),
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.End
            )
        }

        // Additional details row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Market Cap",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = formatCurrency(coin.marketCap),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Volume (24h)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = formatCurrency(coin.volume24h),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dominance",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "${String.format("%.2f", coin.dominancePercentage)}%",
                    style = MaterialTheme.typography.bodySmall
                )
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

class CurrencyFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value)
    }
}

private fun formatPercentage(value: Double): String {
    val prefix = if (value >= 0) "+" else ""
    return "$prefix${String.format("%.2f", value)}%"
}

private fun getPercentageColor(value: Double): Color {
    return when {
        value > 0 -> Color(0xFF4CAF50)  // Green
        value < 0 -> Color(0xFFE53935)  // Red
        else -> Color.Unspecified
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