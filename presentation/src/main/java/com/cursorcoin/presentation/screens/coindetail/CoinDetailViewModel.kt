package com.cursorcoin.presentation.screens.coindetail

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.usecase.GetCoinDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase
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
                .collect { result ->
                    setState {
                        copy(
                            coinDetail = result.data,
                            isLoading = result is com.cursorcoin.core.Resource.Loading,
                            error = if (result is com.cursorcoin.core.Resource.Error) result.message else null
                        )
                    }
                }
        }
    }
}

data class CoinDetailState(
    val coinDetail: CoinDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CoinDetailEvent {
    data class LoadCoinDetail(val coinId: String) : CoinDetailEvent()
} 