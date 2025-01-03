package com.cursorcoin.domain.repository

import com.cursorcoin.domain.model.Coin
import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.model.CoinHistory
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<List<Coin>>
    suspend fun refreshCoins()
    suspend fun shouldRefreshCoins(): Boolean
    suspend fun getCoinById(coinId: String): CoinDetail
    fun getCoinHistory(coinId: String): Flow<List<CoinHistory>>
    suspend fun refreshCoinHistory(coinId: String)
    suspend fun shouldRefreshCoinHistory(coinId: String): Boolean
    fun getUseLocalData(): Flow<Boolean>
    suspend fun setUseLocalData(useLocal: Boolean)
} 