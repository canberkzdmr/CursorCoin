package com.cursorcoin.presentation.screens.coindetail

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.core.Resource
import com.cursorcoin.domain.usecase.GetCoinDetailUseCase
import com.cursorcoin.domain.usecase.GetCoinHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinHistoryUseCase: GetCoinHistoryUseCase
) : BaseViewModel<CoinDetailState, CoinDetailEvent>() {

    override fun createInitialState(): CoinDetailState = CoinDetailState()

    override fun handleEvent(event: CoinDetailEvent) {
        when (event) {
            is CoinDetailEvent.LoadCoinDetail -> loadCoinDetail(event.coinId)
        }
    }

    private fun loadCoinDetail(coinId: String) {
        launch {
            getCoinDetailUseCase(coinId)
                .onStart { setState { copy(isLoading = true) } }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            setState {
                                copy(
                                    coinDetail = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                            loadCoinHistory(coinId)
                        }
                        is Resource.Error -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    error = result.message
                                )
                            }
                        }
                        is Resource.Loading -> {
                            setState {
                                copy(isLoading = true)
                            }
                        }
                    }
                }
        }
    }

    private fun loadCoinHistory(coinId: String) {
        launch {
            getCoinHistoryUseCase(GetCoinHistoryUseCase.Params(coinId))
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            setState {
                                copy(
                                    priceHistory = result.data ?: emptyList(),
                                    error = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            setState {
                                copy(error = result.message)
                            }
                        }
                        is Resource.Loading -> {
                            // Already showing loading state from detail
                        }
                    }
                }
        }
    }
} 