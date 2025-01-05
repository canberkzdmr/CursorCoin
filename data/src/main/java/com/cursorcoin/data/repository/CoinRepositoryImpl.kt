package com.cursorcoin.data.repository

import com.cursorcoin.core.Resource
import com.cursorcoin.data.local.SettingsDataStore
import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.dao.CoinHistoryDao
import com.cursorcoin.data.local.entity.CoinEntity
import com.cursorcoin.data.local.entity.CoinHistoryEntity
import com.cursorcoin.data.remote.api.CoinGeckoApi
import com.cursorcoin.data.remote.dto.toCoin
import com.cursorcoin.data.remote.dto.toCoinHistory
import com.cursorcoin.domain.model.Coin
import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.model.CoinHistory
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val coinDao: CoinDao,
    private val coinHistoryDao: CoinHistoryDao,
    private val settings: SettingsDataStore
) : CoinRepository {

    override fun getCoins(): Flow<List<Coin>> {
        return coinDao.getAllCoins().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshCoins() {
        try {
            val coins = api.getCoins()
            val entities = coins.map { coinDto ->
                CoinEntity(
                    id = coinDto.id,
                    symbol = coinDto.symbol,
                    name = coinDto.name,
                    image = coinDto.image,
                    currentPrice = coinDto.currentPrice,
                    marketCap = coinDto.marketCap,
                    marketCapRank = coinDto.marketCapRank,
                    priceChangePercentage24h = coinDto.priceChangePercentage24h,
                    priceChange24h = coinDto.priceChange24h,
                    priceChangePercentage30d = coinDto.priceChangePercentage30d,
                    high24h = coinDto.high24h,
                    low24h = coinDto.low24h,
                    totalVolume = coinDto.totalVolume,
                    circulatingSupply = coinDto.circulatingSupply,
                    totalSupply = coinDto.totalSupply,
                    maxSupply = coinDto.maxSupply
                )
            }
            coinDao.insertCoins(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun shouldRefreshCoins(): Boolean {
        val lastCoin = coinDao.getLastUpdatedCoin()
        return lastCoin == null || System.currentTimeMillis() - lastCoin.lastUpdated > REFRESH_INTERVAL
    }

    override suspend fun getCoinById(coinId: String): CoinDetail {
        return try {
            val coinDto = api.getCoinById(coinId)
            CoinDetail(
                id = coinDto.id,
                symbol = coinDto.symbol,
                name = coinDto.name,
                description = "", // TODO: Add description from API
                image = coinDto.image,
                currentPrice = coinDto.currentPrice,
                marketCap = coinDto.marketCap,
                marketCapRank = coinDto.marketCapRank,
                priceChangePercentage24h = coinDto.priceChangePercentage24h,
                priceChange24h = coinDto.priceChange24h,
                priceChangePercentage30d = coinDto.priceChangePercentage30d,
                high24h = coinDto.high24h,
                low24h = coinDto.low24h,
                totalVolume = coinDto.totalVolume,
                circulatingSupply = coinDto.circulatingSupply,
                totalSupply = coinDto.totalSupply,
                maxSupply = coinDto.maxSupply
            )
        } catch (e: Exception) {
            val coinEntity = coinDao.getCoinById(coinId)
            if (coinEntity != null) {
                CoinDetail(
                    id = coinEntity.id,
                    symbol = coinEntity.symbol,
                    name = coinEntity.name,
                    description = "", // No description in local database
                    image = coinEntity.image,
                    currentPrice = coinEntity.currentPrice,
                    marketCap = coinEntity.marketCap,
                    marketCapRank = coinEntity.marketCapRank,
                    priceChangePercentage24h = coinEntity.priceChangePercentage24h,
                    priceChange24h = coinEntity.priceChange24h,
                    priceChangePercentage30d = coinEntity.priceChangePercentage30d,
                    high24h = coinEntity.high24h,
                    low24h = coinEntity.low24h,
                    totalVolume = coinEntity.totalVolume,
                    circulatingSupply = coinEntity.circulatingSupply,
                    totalSupply = coinEntity.totalSupply,
                    maxSupply = coinEntity.maxSupply
                )
            } else {
                throw e
            }
        }
    }

    override fun getCoinHistory(coinId: String): Flow<List<CoinHistory>> {
        return coinHistoryDao.getCoinHistory(coinId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshCoinHistory(coinId: String) {
        try {
            val historyDto = api.getCoinHistory(coinId)
            val history = historyDto.toCoinHistory()
            val entities = history.map { coinHistory ->
                CoinHistoryEntity(
                    coinId = coinId,
                    timestamp = coinHistory.timestamp,
                    price = coinHistory.price
                )
            }
            coinHistoryDao.insertCoinHistory(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun shouldRefreshCoinHistory(coinId: String): Boolean {
        val lastHistory = coinHistoryDao.getLastUpdatedHistory(coinId)
        return lastHistory == null || System.currentTimeMillis() - lastHistory.timestamp > REFRESH_INTERVAL
    }

    override fun getUseLocalData(): Flow<Boolean> = settings.useLocalData

    override suspend fun setUseLocalData(useLocal: Boolean) {
        settings.setUseLocalData(useLocal)
    }

    companion object {
        private const val REFRESH_INTERVAL = 5 * 60 * 1000 // 5 minutes
    }
} 