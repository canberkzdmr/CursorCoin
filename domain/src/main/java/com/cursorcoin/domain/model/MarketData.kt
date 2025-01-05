package com.cursorcoin.domain.model

data class MarketData(
    val totalMarketCap: Double,
    val totalVolume: Double,
    val btcDominance: Double,
    val marketCapPercentageChange24h: Double,
    val timestamp: Long,
    val activeCryptocurrencies: Int,
    val markets: Int,
    val topCoinsDominance: Map<String, Double> // Market dominance percentages for top coins
)