package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCoinToPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    data class Params(
        val coinId: String,
        val amount: Double,
        val purchasePrice: Double
    )

    operator fun invoke(params: Params): Flow<Resource<Unit>> = repository.addCoinToPortfolio(
        coinId = params.coinId,
        amount = params.amount,
        purchasePrice = params.purchasePrice
    )
} 