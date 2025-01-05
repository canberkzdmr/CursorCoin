package com.cursorcoin.data.remote.api

import com.cursorcoin.data.remote.model.MarketDataDto
import com.cursorcoin.data.remote.dto.CoinDto
import com.cursorcoin.data.remote.dto.CoinHistoryDto
import com.cursorcoin.data.remote.model.GlobalMarketDataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("price_change_percentage") priceChangePercentage: String = "24h,30d"
    ): List<CoinDto>

    @GET("coins/{id}")
    suspend fun getCoinById(
        @Path("id") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = false
    ): CoinDto

    @GET("coins/{id}/market_chart")
    suspend fun getCoinHistory(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int = 30,
        @Query("interval") interval: String = "daily"
    ): CoinHistoryDto

    @GET("global")
    suspend fun getGlobalMarketData(): GlobalMarketDataResponse
} 