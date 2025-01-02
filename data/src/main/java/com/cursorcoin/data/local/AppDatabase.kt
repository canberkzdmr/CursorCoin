package com.cursorcoin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.entity.CoinEntity
import com.cursorcoin.data.local.entity.CoinHistoryEntity

@Database(
    entities = [CoinEntity::class, CoinHistoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
} 