package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveCoinFromPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    operator fun invoke(portfolio: Portfolio): Flow<Resource<Unit>> = repository.removeCoinFromPortfolio(portfolio)
} 