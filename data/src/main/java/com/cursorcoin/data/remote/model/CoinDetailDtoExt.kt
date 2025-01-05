package com.cursorcoin.data.remote.model

import com.cursorcoin.domain.model.CoinDetail

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        id = id,
        symbol = symbol,
        name = name,
        description = description["en"] ?: "",
        image = image.large,
        currentPrice = marketData.currentPrice["usd"] ?: 0.0,
        marketCap = marketData.marketCap["usd"] ?: 0L,
        marketCapRank = marketData.marketCapRank,
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        priceChange24h = marketData.priceChange24h,
        priceChangePercentage30d = marketData.priceChangePercentage30d,
        high24h = marketData.high24h["usd"] ?: 0.0,
        low24h = marketData.low24h["usd"] ?: 0.0,
        totalVolume = marketData.totalVolume["usd"] ?: 0.0,
        circulatingSupply = marketData.circulatingSupply,
        totalSupply = marketData.totalSupply,
        maxSupply = marketData.maxSupply
    )
} 