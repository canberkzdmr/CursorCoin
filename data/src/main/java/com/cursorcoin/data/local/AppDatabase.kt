package com.cursorcoin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.dao.CoinHistoryDao
import com.cursorcoin.data.local.dao.PortfolioDao
import com.cursorcoin.data.local.entity.CoinEntity
import com.cursorcoin.data.local.entity.CoinHistoryEntity
import com.cursorcoin.data.local.entity.PortfolioEntity

@Database(
    entities = [
        CoinEntity::class,
        CoinHistoryEntity::class,
        PortfolioEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun coinHistoryDao(): CoinHistoryDao
    abstract fun portfolioDao(): PortfolioDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new columns to coins table
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN priceChangePercentage30d REAL
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN high24h REAL NOT NULL DEFAULT 0.0
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN low24h REAL NOT NULL DEFAULT 0.0
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN totalVolume REAL NOT NULL DEFAULT 0.0
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN circulatingSupply REAL
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN totalSupply REAL
                """)
                database.execSQL("""
                    ALTER TABLE coins ADD COLUMN maxSupply REAL
                """)

                // Create coin_history table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS coin_history (
                        coinId TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        price REAL NOT NULL,
                        PRIMARY KEY(coinId, timestamp)
                    )
                """)
            }
        }
    }
} 