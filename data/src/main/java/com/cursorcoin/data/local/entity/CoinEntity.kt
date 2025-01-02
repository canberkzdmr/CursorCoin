package com.cursorcoin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cursorcoin.domain.model.Coin

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int,
    val priceChangePercentage24h: Double?,
    val priceChange24h: Double?,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain() = Coin(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        priceChangePercentage24h = priceChangePercentage24h,
        priceChange24h = priceChange24h
    )
} 