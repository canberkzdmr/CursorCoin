package com.cursorcoin.domain.model

data class Portfolio(
    val id: Long = 0,
    val coinId: String,
    val amount: Double,
    val purchasePrice: Double,
    val currentPrice: Double = 0.0,
    val currentValue: Double = currentPrice * amount,
    val profitLoss: Double = currentValue - (purchasePrice * amount),
    val profitLossPercentage: Double = if (purchasePrice > 0) (profitLoss / (purchasePrice * amount)) * 100 else 0.0
)

data class PortfolioHistoryItem(
    val timestamp: Long,
    val totalValue: Double
) 