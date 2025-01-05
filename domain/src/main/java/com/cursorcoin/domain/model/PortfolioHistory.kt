package com.cursorcoin.domain.model

data class PortfolioHistory(
    val timestamp: Long,
    val totalValue: Double,
    val profitLoss: Double
) 