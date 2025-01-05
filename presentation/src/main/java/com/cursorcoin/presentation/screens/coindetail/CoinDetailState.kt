package com.cursorcoin.presentation.screens.coindetail

import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.model.CoinHistory

data class CoinDetailState(
    val coinDetail: CoinDetail? = null,
    val priceHistory: List<CoinHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 