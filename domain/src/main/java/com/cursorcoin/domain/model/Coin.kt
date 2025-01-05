package com.cursorcoin.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
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
    val maxSupply: Double?
) : Parcelable 