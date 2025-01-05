package com.cursorcoin.domain.repository

import com.cursorcoin.core.Resource
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.domain.usecase.GetPortfolioSummaryUseCase
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getPortfolioSummary(): Flow<Resource<GetPortfolioSummaryUseCase.Result>>
    fun addCoinToPortfolio(coinId: String, amount: Double, purchasePrice: Double): Flow<Resource<Unit>>
    fun removeCoinFromPortfolio(portfolio: Portfolio): Flow<Resource<Unit>>
} 