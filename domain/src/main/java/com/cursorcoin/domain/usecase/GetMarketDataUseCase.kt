package com.cursorcoin.domain.usecase

import com.cursorcoin.domain.model.MarketData
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMarketDataUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<MarketData> = flow {
        try {
            val marketData = repository.getMarketData()
            emit(marketData)
        } catch (e: Exception) {
            throw e
        }
    }
}