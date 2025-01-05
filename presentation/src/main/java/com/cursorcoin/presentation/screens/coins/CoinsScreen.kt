package com.cursorcoin.presentation.screens.coins

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import com.cursorcoin.domain.model.Coin
import com.cursorcoin.presentation.navigation.Screen
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: CoinsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    var showSelectDialog by remember { mutableStateOf(false) }
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(CoinsEvent.LoadCoins)
    }

    if (showSelectDialog && selectedCoin != null) {
        AlertDialog(
            onDismissRequest = {
                showSelectDialog = false
                selectedCoin = null
            },
            title = { Text("Add to Portfolio?") },
            text = {
                Text("Would you like to add ${selectedCoin?.name} to your portfolio?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedCoin?.let { coin ->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_coin_id", coin.id)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_coin_name", coin.name)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_coin_symbol", coin.symbol)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_coin_image", coin.image)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_coin_price", coin.currentPrice)
                        }
                        navController.navigateUp()
                        showSelectDialog = false
                        selectedCoin = null
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSelectDialog = false
                        selectedCoin = null
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coins") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
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
            } else if (state.coins.isEmpty()) {
                Text(
                    text = "No coins available",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.coins) { coin ->
                        CoinItem(
                            coin = coin,
                            onItemClick = { onNavigateToDetail(coin.id) },
                            onLongClick = {
                                selectedCoin = coin
                                showSelectDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoinItem(
    coin: Coin,
    onItemClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = coin.image,
                    contentDescription = coin.name,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = coin.symbol.uppercase(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatPrice(coin.currentPrice),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatPercentage(coin.priceChangePercentage24h),
                    color = getPercentageColor(coin.priceChangePercentage24h),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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

private fun getPercentageColor(percentage: Double?): Color {
    return when {
        percentage == null -> Color.Gray
        percentage >= 0 -> Color(0xFF4CAF50)  // Green
        else -> Color(0xFFE53935)  // Red
    }
} 