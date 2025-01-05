package com.cursorcoin.presentation.screens.coindetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.cursorcoin.presentation.components.LineChart
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    navController: NavController,
    coinId: String,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(coinId) {
        viewModel.handleEvent(CoinDetailEvent.LoadCoinDetail(coinId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.coinDetail?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(
                    text = state.error?.let { "Error: $it" } ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            } else {
                state.coinDetail?.let { coin ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Coin Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = coin.name,
                                modifier = Modifier.size(64.dp)
                            )
                            Column {
                                Text(
                                    text = coin.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = coin.symbol.uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Price Info
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Current Price",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = formatPrice(coin.currentPrice),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "24h Change",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = formatPercentage(coin.priceChangePercentage24h),
                                            color = getPercentageColor(coin.priceChangePercentage24h)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "30d Change",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = formatPercentage(coin.priceChangePercentage30d),
                                            color = getPercentageColor(coin.priceChangePercentage30d)
                                        )
                                    }
                                }
                            }
                        }

                        // Price Chart
                        if (state.priceHistory.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                val entries = state.priceHistory
                                    .sortedBy { it.timestamp }
                                    .mapIndexed { index, history ->
                                        Entry(index.toFloat(), history.price.toFloat())
                                    }

                                if (entries.isNotEmpty()) {
                                    val dataSet = LineDataSet(entries, "Price").apply {
                                        color = android.graphics.Color.BLUE
                                        setDrawCircles(false)
                                        setDrawValues(false)
                                        mode = LineDataSet.Mode.CUBIC_BEZIER
                                        lineWidth = 2f
                                        setDrawFilled(true)
                                        fillColor = android.graphics.Color.BLUE
                                        fillAlpha = 30
                                    }

                                    LineChart(
                                        lineData = LineData(dataSet),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }

                        // Market Info
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Market Info",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Market Cap",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(text = formatPrice(coin.marketCap.toDouble()))
                                    }
                                    Column {
                                        Text(
                                            text = "Volume",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(text = formatPrice(coin.totalVolume))
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "24h High",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(text = formatPrice(coin.high24h))
                                    }
                                    Column {
                                        Text(
                                            text = "24h Low",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(text = formatPrice(coin.low24h))
                                    }
                                }

                                // Supply Information
                                SupplyInfoRow(
                                    label = "Circulating Supply",
                                    value = coin.circulatingSupply
                                )
                                SupplyInfoRow(
                                    label = "Total Supply",
                                    value = coin.totalSupply
                                )
                                SupplyInfoRow(
                                    label = "Max Supply",
                                    value = coin.maxSupply
                                )
                            }
                        }

                        // About Section
                        if (!coin.description.isNullOrBlank()) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "About ${coin.name}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = coin.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SupplyInfoRow(
    label: String,
    value: Double?
) {
    value?.let {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = formatNumber(it))
        }
    }
}

private fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
}

private fun formatPercentage(percentage: Double?): String {
    return if (percentage != null) {
        String.format("%.2f%%", percentage)
    } else {
        "N/A"
    }
}

private fun formatNumber(number: Double): String {
    return NumberFormat.getNumberInstance(Locale.US).format(number)
}

private fun getPercentageColor(percentage: Double?): Color {
    return when {
        percentage == null -> Color.Gray
        percentage >= 0 -> Color(0xFF4CAF50)  // Green
        else -> Color(0xFFE53935)  // Red
    }
} 