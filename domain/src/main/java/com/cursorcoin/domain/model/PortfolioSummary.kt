package com.cursorcoin.domain.model

data class PortfolioSummary(
    val totalValue: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val totalProfitLoss: Double = totalValue - totalInvestment,
    val totalProfitLossPercentage: Double = if (totalInvestment > 0) (totalProfitLoss / totalInvestment) * 100 else 0.0,
    val portfolioHistory: List<PortfolioHistoryItem> = emptyList()
) 