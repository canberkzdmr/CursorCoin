package com.cursorcoin.data.repository

import com.cursorcoin.core.Resource
import com.cursorcoin.data.local.dao.CoinDao
import com.cursorcoin.data.local.dao.PortfolioDao
import com.cursorcoin.data.local.entity.PortfolioEntity
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.domain.model.PortfolioSummary
import com.cursorcoin.domain.repository.PortfolioRepository
import com.cursorcoin.domain.usecase.GetPortfolioSummaryUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val portfolioDao: PortfolioDao,
    private val coinDao: CoinDao
) : PortfolioRepository {

    override fun getPortfolioSummary(): Flow<Resource<GetPortfolioSummaryUseCase.Result>> = flow {
        emit(Resource.Loading())
        try {
            portfolioDao.getAllPortfolioItems()
                .combine(coinDao.getAllCoins()) { portfolioItems, coins ->
                    val portfolioList = portfolioItems.map { entity ->
                        val currentPrice = coins.find { it.id == entity.coinId }?.currentPrice ?: 0.0
                        entity.toDomain(currentPrice)
                    }

                    val totalInvestment = portfolioDao.getTotalInvestment() ?: 0.0
                    val totalValue = portfolioList.sumOf { it.currentValue }

                    GetPortfolioSummaryUseCase.Result(
                        portfolio = portfolioList,
                        summary = PortfolioSummary(
                            totalValue = totalValue,
                            totalInvestment = totalInvestment
                        )
                    )
                }
                .collect { result ->
                    emit(Resource.Success(result))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun addCoinToPortfolio(
        coinId: String,
        amount: Double,
        purchasePrice: Double
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val portfolioEntity = PortfolioEntity(
                coinId = coinId,
                amount = amount,
                purchasePrice = purchasePrice
            )
            portfolioDao.insertPortfolioItem(portfolioEntity)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun removeCoinFromPortfolio(portfolio: Portfolio): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val entity = PortfolioEntity(
                id = portfolio.id,
                coinId = portfolio.coinId,
                amount = portfolio.amount,
                purchasePrice = portfolio.purchasePrice
            )
            portfolioDao.deletePortfolioItem(entity)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
} 