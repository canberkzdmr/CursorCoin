package com.cursorcoin.data.local.dao

import androidx.room.*
import com.cursorcoin.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio")
    fun getAllPortfolioItems(): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioItem(item: PortfolioEntity): Long

    @Delete
    suspend fun deletePortfolioItem(item: PortfolioEntity)

    @Query("SELECT * FROM portfolio WHERE coinId = :coinId")
    suspend fun getPortfolioItemByCoinId(coinId: String): PortfolioEntity?

    @Query("SELECT SUM(amount * purchasePrice) FROM portfolio")
    suspend fun getTotalInvestment(): Double?
} 