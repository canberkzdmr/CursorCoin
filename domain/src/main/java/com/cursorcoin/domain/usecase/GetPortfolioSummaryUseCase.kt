package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.domain.model.PortfolioSummary
import com.cursorcoin.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPortfolioSummaryUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    data class Result(
        val portfolio: List<Portfolio>,
        val summary: PortfolioSummary
    )

    operator fun invoke(params: Unit): Flow<Resource<Result>> = repository.getPortfolioSummary()
} 