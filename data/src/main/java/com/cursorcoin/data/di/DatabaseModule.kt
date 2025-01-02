package com.cursorcoin.data.di

import android.content.Context
import androidx.room.Room
import com.cursorcoin.data.local.AppDatabase
import com.cursorcoin.data.local.dao.CoinDao
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
        "cursor_coin_db"
    ).build()

    @Provides
    @Singleton
    fun provideCoinDao(database: AppDatabase): CoinDao = database.coinDao()
} 