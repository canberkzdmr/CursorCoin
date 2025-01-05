package com.cursorcoin.domain.model

data class MarketData(
    val totalMarketCap: Double,
    val totalVolume: Double,
    val btcDominance: Double,
    val marketCapPercentageChange24h: Double,
    val timestamp: Long,
    val activeCryptocurrencies: Int,
    val markets: Int,
    val topCoinsDominance: Map<String, Double>, // Market dominance percentages for top coins
    val marketCapByCurrency: Map<String, Double>, // Market cap in different currencies
    val volumeByCurrency: Map<String, Double>, // Volume in different currencies
    val defiMarketCap: Double, // DeFi market cap in USD
    val defiDominance: Double, // DeFi dominance percentage
    val topCoins: List<TopCoin> // Top cryptocurrencies by market cap
)

data class TopCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Double,
    val marketCapRank: Int,
    val volume24h: Double,
    val percentageChange24h: Double,
    val percentageChange7d: Double?,
    val percentageChange30d: Double?,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val dominancePercentage: Double
)