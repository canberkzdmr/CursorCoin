package com.cursorcoin.data.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val description: Map<String, String>,
    @SerializedName("market_data")
    val marketData: MarketData,
    val image: CoinImage
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>,
    @SerializedName("market_cap")
    val marketCap: Map<String, Long>,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,
    @SerializedName("price_change_24h")
    val priceChange24h: Double?,
    @SerializedName("price_change_percentage_30d")
    val priceChangePercentage30d: Double?,
    @SerializedName("high_24h")
    val high24h: Map<String, Double>,
    @SerializedName("low_24h")
    val low24h: Map<String, Double>,
    @SerializedName("total_volume")
    val totalVolume: Map<String, Double>,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("max_supply")
    val maxSupply: Double?
)

data class CoinImage(
    val thumb: String,
    val small: String,
    val large: String
) 