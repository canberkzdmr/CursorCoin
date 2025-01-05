package com.cursorcoin.data.local.entity

import androidx.room.Entity
import com.cursorcoin.domain.model.CoinHistory

@Entity(
    tableName = "coin_history",
    primaryKeys = ["coinId", "timestamp"]
)
data class CoinHistoryEntity(
    val coinId: String,
    val timestamp: Long,
    val price: Double
) {
    fun toDomain() = CoinHistory(
        timestamp = timestamp,
        price = price
    )
} 