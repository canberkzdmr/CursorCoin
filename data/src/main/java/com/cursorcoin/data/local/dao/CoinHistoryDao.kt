package com.cursorcoin.data.local.dao

import androidx.room.*
import com.cursorcoin.data.local.entity.CoinHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinHistoryDao {
    @Query("SELECT * FROM coin_history WHERE coinId = :coinId ORDER BY timestamp DESC")
    fun getCoinHistory(coinId: String): Flow<List<CoinHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinHistory(history: List<CoinHistoryEntity>)

    @Query("SELECT * FROM coin_history WHERE coinId = :coinId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastUpdatedHistory(coinId: String): CoinHistoryEntity?
} 