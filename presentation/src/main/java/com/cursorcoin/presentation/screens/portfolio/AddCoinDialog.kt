package com.cursorcoin.presentation.screens.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoinDialog(
    onDismiss: () -> Unit,
    onConfirm: (coinId: String, amount: Double, price: Double) -> Unit,
    onNavigateToCoins: () -> Unit,
    selectedCoinInfo: SelectedCoinInfo? = null
) {
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    if (selectedCoinInfo == null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add Coin to Portfolio") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Please select a coin first")
                    Button(
                        onClick = onNavigateToCoins,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Coin")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add ${selectedCoinInfo.name} to Portfolio") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Coin Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = selectedCoinInfo.image,
                            contentDescription = selectedCoinInfo.name,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = selectedCoinInfo.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = selectedCoinInfo.symbol.uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Divider()

                    // Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Price Input (prefilled with current price)
                    OutlinedTextField(
                        value = price.ifEmpty { selectedCoinInfo.currentPrice.toString() },
                        onValueChange = { price = it },
                        label = { Text("Purchase Price (USD)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amountDouble = amount.toDoubleOrNull()
                        val priceDouble = price.ifEmpty { selectedCoinInfo.currentPrice.toString() }.toDoubleOrNull()
                        if (amountDouble != null && priceDouble != null) {
                            onConfirm(selectedCoinInfo.id, amountDouble, priceDouble)
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
} 