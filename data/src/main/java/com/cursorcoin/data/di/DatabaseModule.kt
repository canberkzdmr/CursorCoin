package com.cursorcoin.data.di

import android.content.Context
import androidx.room.Room
import com.cursorcoin.data.local.AppDatabase
import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.dao.CoinHistoryDao
import com.cursorcoin.data.local.dao.PortfolioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "cursor_coin.db"
    )
    .addMigrations(AppDatabase.MIGRATION_1_2)
    .build()

    @Provides
    @Singleton
    fun provideCoinDao(database: AppDatabase): CoinDao = database.coinDao()

    @Provides
    @Singleton
    fun provideCoinHistoryDao(database: AppDatabase): CoinHistoryDao = database.coinHistoryDao()

    @Provides
    @Singleton
    fun providePortfolioDao(database: AppDatabase): PortfolioDao = database.portfolioDao()
} 