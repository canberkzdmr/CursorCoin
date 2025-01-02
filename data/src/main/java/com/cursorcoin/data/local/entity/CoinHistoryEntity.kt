package com.cursorcoin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cursorcoin.domain.model.CoinHistory

@Entity(tableName = "coin_history")
data class CoinHistoryEntity(
    @PrimaryKey
    val id: String,
    val coinId: String,
    val timestamp: Long,
    val price: Double,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain() = CoinHistory(
        timestamp = timestamp,
        price = price
    )
} 