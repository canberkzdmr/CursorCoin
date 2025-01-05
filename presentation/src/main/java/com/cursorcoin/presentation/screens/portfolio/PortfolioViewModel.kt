package com.cursorcoin.presentation.screens.portfolio

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.core.Resource
import com.cursorcoin.domain.model.Portfolio
import com.cursorcoin.domain.model.PortfolioSummary
import com.cursorcoin.domain.usecase.AddCoinToPortfolioUseCase
import com.cursorcoin.domain.usecase.GetPortfolioSummaryUseCase
import com.cursorcoin.domain.usecase.RemoveCoinFromPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioSummaryUseCase: GetPortfolioSummaryUseCase,
    private val addCoinToPortfolioUseCase: AddCoinToPortfolioUseCase,
    private val removeCoinFromPortfolioUseCase: RemoveCoinFromPortfolioUseCase
) : BaseViewModel<PortfolioState, PortfolioEvent>() {

    override fun createInitialState(): PortfolioState = PortfolioState()

    init {
        loadPortfolio()
    }

    override fun handleEvent(event: PortfolioEvent) {
        when (event) {
            is PortfolioEvent.AddCoin -> addCoin(event.coinId, event.amount, event.price)
            is PortfolioEvent.RemoveCoin -> removeCoin(event.portfolio)
            is PortfolioEvent.RefreshPortfolio -> loadPortfolio()
        }
    }

    private fun loadPortfolio() {
        launch {
            getPortfolioSummaryUseCase(Unit)
                .collect { result ->
                    setState {
                        copy(
                            portfolio = result.data?.portfolio ?: emptyList(),
                            portfolioSummary = result.data?.summary ?: PortfolioSummary(
                                totalValue = 0.0,
                                totalInvestment = 0.0,
                                totalProfitLoss = 0.0,
                                totalProfitLossPercentage = 0.0,
                                portfolioHistory = emptyList()
                            ),
                            isLoading = result is Resource.Loading<*>,
                            error = if (result is Resource.Error<*>) result.message else null
                        )
                    }
                }
        }
    }

    private fun addCoin(coinId: String, amount: Double, price: Double) {
        launch {
            addCoinToPortfolioUseCase(
                AddCoinToPortfolioUseCase.Params(
                    coinId = coinId,
                    amount = amount,
                    purchasePrice = price
                )
            ).collect { result ->
                if (result is Resource.Success<*>) {
                    loadPortfolio()
                }
            }
        }
    }

    private fun removeCoin(portfolio: Portfolio) {
        launch {
            removeCoinFromPortfolioUseCase(portfolio)
                .collect { result ->
                    if (result is Resource.Success<*>) {
                        loadPortfolio()
                    }
                }
        }
    }
}

data class PortfolioState(
    val portfolio: List<Portfolio> = emptyList(),
    val portfolioSummary: PortfolioSummary = PortfolioSummary(
        totalValue = 0.0,
        totalInvestment = 0.0,
        totalProfitLoss = 0.0,
        totalProfitLossPercentage = 0.0,
        portfolioHistory = emptyList()
    ),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class PortfolioEvent {
    data class AddCoin(val coinId: String, val amount: Double, val price: Double) : PortfolioEvent()
    data class RemoveCoin(val portfolio: Portfolio) : PortfolioEvent()
    object RefreshPortfolio : PortfolioEvent()
} 