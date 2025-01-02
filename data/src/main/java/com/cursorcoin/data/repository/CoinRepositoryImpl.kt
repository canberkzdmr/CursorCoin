package com.cursorcoin.data.repository

import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.entity.CoinEntity
import com.cursorcoin.data.local.entity.CoinHistoryEntity
import com.cursorcoin.data.local.preferences.SettingsPreferences
import com.cursorcoin.data.remote.api.CoinGeckoApi
import com.cursorcoin.data.remote.model.CoinDetailDto
import com.cursorcoin.data.remote.model.CoinDto
import com.cursorcoin.domain.model.Coin
import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.model.CoinHistory
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val dao: CoinDao,
    private val preferences: SettingsPreferences
) : CoinRepository {

    override fun getCoins(): Flow<List<Coin>> =
        dao.getAllCoins().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refreshCoins() {
        try {
            val remoteCoins = api.getCoins()
            dao.insertCoins(remoteCoins.map { it.toEntity() })
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun shouldRefreshCoins(): Boolean {
        val lastUpdateTime = dao.getLastUpdateTime() ?: 0L
        val currentTime = System.currentTimeMillis()
        return currentTime - lastUpdateTime > TimeUnit.MINUTES.toMillis(15)
    }

    override suspend fun getCoinById(coinId: String): CoinDetail {
        return api.getCoinById(coinId).toDomain()
    }

    override fun getCoinHistory(coinId: String): Flow<List<CoinHistory>> =
        dao.getCoinHistory(coinId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refreshCoinHistory(coinId: String) {
        try {
            val history = api.getCoinMarketChart(
                coinId,
                days = "30",
                interval = "daily"
            ).prices.map { (timestamp, price) ->
                CoinHistoryEntity(
                    id = "${coinId}_${timestamp}",
                    coinId = coinId,
                    timestamp = timestamp,
                    price = price
                )
            }
            dao.deleteCoinHistory(coinId)
            dao.insertCoinHistory(history)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun shouldRefreshHistory(coinId: String): Boolean {
        val lastUpdateTime = dao.getHistoryLastUpdateTime(coinId) ?: 0L
        val currentTime = System.currentTimeMillis()
        return currentTime - lastUpdateTime > TimeUnit.HOURS.toMillis(1)
    }

    override fun getUseLocalData(): Flow<Boolean> = preferences.useLocalData

    override suspend fun setUseLocalData(useLocal: Boolean) {
        preferences.setUseLocalData(useLocal)
    }

    private fun CoinDto.toEntity() = CoinEntity(
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

    private fun CoinDetailDto.toDomain() = CoinDetail(
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