package com.cursorcoin.presentation.screens.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.presentation.navigation.Screen
import java.text.NumberFormat
import java.util.*

data class SelectedCoinInfo(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCoinInfo by remember { mutableStateOf<SelectedCoinInfo?>(null) }

    LaunchedEffect(navController.currentBackStackEntry) {
        // Get selected coin properties from navigation arguments if available
        val coinId = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_coin_id")
        val coinName = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_coin_name")
        val coinSymbol = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_coin_symbol")
        val coinImage = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_coin_image")
        val coinPrice = navController.currentBackStackEntry?.savedStateHandle?.get<Double>("selected_coin_price")

        if (coinId != null && coinName != null && coinSymbol != null && coinImage != null && coinPrice != null) {
            selectedCoinInfo = SelectedCoinInfo(
                id = coinId,
                name = coinName,
                symbol = coinSymbol,
                image = coinImage,
                currentPrice = coinPrice
            )
            showAddDialog = true
            
            // Clear the saved state
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_coin_id")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_coin_name")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_coin_symbol")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_coin_image")
            navController.currentBackStackEntry?.savedStateHandle?.remove<Double>("selected_coin_price")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Coin")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.portfolio.isEmpty()) {
                Text(
                    text = "No coins in portfolio",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.portfolio) { portfolio ->
                        PortfolioItem(
                            portfolio = portfolio,
                            onRemove = { viewModel.handleEvent(PortfolioEvent.RemoveCoin(portfolio)) }
                        )
                    }
                }
            }

            if (showAddDialog) {
                AddCoinDialog(
                    onDismiss = {
                        showAddDialog = false
                        selectedCoinInfo = null
                    },
                    onConfirm = { coinId, amount, price ->
                        viewModel.handleEvent(PortfolioEvent.AddCoin(coinId, amount, price))
                        showAddDialog = false
                        selectedCoinInfo = null
                    },
                    onNavigateToCoins = {
                        navController.navigate(Screen.CoinList.route)
                        showAddDialog = false
                    },
                    selectedCoinInfo = selectedCoinInfo
                )
            }
        }
    }
}

@Composable
fun PortfolioItem(
    portfolio: Portfolio,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = portfolio.coinId,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Amount: ${portfolio.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Purchase Price: ${formatPrice(portfolio.purchasePrice)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            TextButton(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}

private fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
} 