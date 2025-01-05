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
    val priceChangePercentage30d: Double?,
    val high24h: Double,
    val low24h: Double,
    val totalVolume: Double,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
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
        priceChange24h = priceChange24h,
        priceChangePercentage30d = priceChangePercentage30d,
        high24h = high24h,
        low24h = low24h,
        totalVolume = totalVolume,
        circulatingSupply = circulatingSupply,
        totalSupply = totalSupply,
        maxSupply = maxSupply
    )

    companion object {
        fun fromDomain(coin: Coin) = CoinEntity(
            id = coin.id,
            symbol = coin.symbol,
            name = coin.name,
            image = coin.image,
            currentPrice = coin.currentPrice,
            marketCap = coin.marketCap,
            marketCapRank = coin.marketCapRank,
            priceChangePercentage24h = coin.priceChangePercentage24h,
            priceChange24h = coin.priceChange24h,
            priceChangePercentage30d = coin.priceChangePercentage30d,
            high24h = coin.high24h,
            low24h = coin.low24h,
            totalVolume = coin.totalVolume,
            circulatingSupply = coin.circulatingSupply,
            totalSupply = coin.totalSupply,
            maxSupply = coin.maxSupply
        )
    }
} 