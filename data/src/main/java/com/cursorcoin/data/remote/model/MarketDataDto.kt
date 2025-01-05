package com.cursorcoin.data.remote.model

import com.cursorcoin.domain.model.MarketData
import com.google.gson.annotations.SerializedName

data class GlobalMarketDataResponse(
    @SerializedName("data")
    val data: MarketDataDto
)

data class MarketDataDto(
    @SerializedName("total_market_cap")
    val totalMarketCap: Map<String, Double>,
    @SerializedName("total_volume")
    val totalVolume: Map<String, Double>,
    @SerializedName("market_cap_percentage")
    val marketCapPercentage: Map<String, Double>,
    @SerializedName("market_cap_change_percentage_24h_usd")
    val marketCapChangePercentage24h: Double,
    @SerializedName("active_cryptocurrencies")
    val activeCryptocurrencies: Int,
    @SerializedName("markets")
    val markets: Int,
    @SerializedName("updated_at")
    val updatedAt: Long
) {
    fun toMarketData(): MarketData = MarketData(
        totalMarketCap = totalMarketCap["usd"] ?: 0.0,
        totalVolume = totalVolume["usd"] ?: 0.0,
        btcDominance = marketCapPercentage["btc"] ?: 0.0,
        marketCapPercentageChange24h = marketCapChangePercentage24h,
        timestamp = updatedAt,
        activeCryptocurrencies = activeCryptocurrencies,
        markets = markets,
        topCoinsDominance = marketCapPercentage
    )
} 