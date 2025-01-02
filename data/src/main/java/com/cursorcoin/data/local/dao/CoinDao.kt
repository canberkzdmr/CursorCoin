package com.cursorcoin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cursorcoin.data.local.entity.CoinEntity
import com.cursorcoin.data.local.entity.CoinHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Query("SELECT MAX(lastUpdated) FROM coins")
    suspend fun getLastUpdateTime(): Long?

    @Query("SELECT * FROM coin_history WHERE coinId = :coinId ORDER BY timestamp ASC")
    fun getCoinHistory(coinId: String): Flow<List<CoinHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinHistory(history: List<CoinHistoryEntity>)

    @Query("SELECT MAX(lastUpdated) FROM coin_history WHERE coinId = :coinId")
    suspend fun getHistoryLastUpdateTime(coinId: String): Long?

    @Query("DELETE FROM coin_history WHERE coinId = :coinId")
    suspend fun deleteCoinHistory(coinId: String)
} 