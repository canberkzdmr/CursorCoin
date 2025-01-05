package com.cursorcoin.data.local.dao

import androidx.room.*
import com.cursorcoin.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM coins ORDER BY marketCapRank")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins WHERE id = :coinId")
    suspend fun getCoinById(coinId: String): CoinEntity?

    @Query("SELECT * FROM coins ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdatedCoin(): CoinEntity?
} 