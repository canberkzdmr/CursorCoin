package com.cursorcoin.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "portfolio_history",
    primaryKeys = ["timestamp"]
)
data class PortfolioHistoryEntity(
    val timestamp: Long,
    val totalValue: Double,
    val profitLoss: Double
) 