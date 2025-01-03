package com.cursorcoin.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "coin_history",
    primaryKeys = ["coinId", "timestamp"]
)
data class CoinHistoryEntity(
    val coinId: String,
    val timestamp: Long,
    val price: Double,
    val lastUpdated: Long = System.currentTimeMillis()
) 