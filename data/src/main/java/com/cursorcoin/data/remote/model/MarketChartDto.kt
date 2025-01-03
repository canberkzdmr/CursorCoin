package com.cursorcoin.data.remote.model

import com.google.gson.annotations.SerializedName

data class MarketChartDto(
    @SerializedName("prices")
    val prices: List<List<Double>>, // [timestamp, price]
    @SerializedName("market_caps")
    val marketCaps: List<List<Double>>, // [timestamp, market_cap]
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double>> // [timestamp, volume]
) 