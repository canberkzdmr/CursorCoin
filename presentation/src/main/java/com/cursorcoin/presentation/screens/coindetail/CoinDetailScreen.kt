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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cursorcoin.domain.model.CoinDetail
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

    LaunchedEffect(key1 = coinId) {
        viewModel.handleEvent(CoinDetailEvent.LoadCoinDetail(coinId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.coinDetail?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.coinDetail != null -> {
                    val coin = state.coinDetail!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header with image and basic info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = coin.name,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = coin.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = coin.symbol.uppercase(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Price information
                        PriceSection(coin)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Market data
                        MarketDataSection(coin)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Description
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = coin.description)
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceSection(coin: CoinDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatPrice(coin.currentPrice),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // 24h price change
            PriceChangeRow(
                percentage = coin.priceChangePercentage24h,
                label = "24h"
            )
            
            // 30d price change
            PriceChangeRow(
                percentage = coin.priceChangePercentage30d,
                label = "30d"
            )
        }
    }
}

@Composable
private fun PriceChangeRow(
    percentage: Double?,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        val color = when {
            percentage == null -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            percentage >= 0 -> Color(0xFF4CAF50)
            else -> Color(0xFFE53935)
        }
        
        Text(
            text = formatPercentage(percentage),
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = " ($label)",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun MarketDataSection(coin: CoinDetail) {
    Column {
        Text(
            text = "Market Data",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        MarketDataItem("Market Cap Rank", "#${coin.marketCapRank}")
        MarketDataItem("Market Cap", formatPrice(coin.marketCap.toDouble()))
        MarketDataItem("24h High", formatPrice(coin.high24h))
        MarketDataItem("24h Low", formatPrice(coin.low24h))
        MarketDataItem("Total Volume", formatPrice(coin.totalVolume))
        coin.circulatingSupply?.let {
            MarketDataItem("Circulating Supply", formatNumber(it))
        }
        coin.totalSupply?.let {
            MarketDataItem("Total Supply", formatNumber(it))
        }
        coin.maxSupply?.let {
            MarketDataItem("Max Supply", formatNumber(it))
        }
    }
}

@Composable
private fun MarketDataItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(text = value)
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