package com.cursorcoin.data.di

import com.cursorcoin.domain.repository.CoinRepository
import com.cursorcoin.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMarketDataUseCase(
        repository: CoinRepository
    ): GetMarketDataUseCase = GetMarketDataUseCase(repository)

    // Other use cases can be added here
} 