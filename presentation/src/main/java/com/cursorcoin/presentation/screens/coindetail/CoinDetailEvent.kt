package com.cursorcoin.presentation.screens.coindetail

sealed class CoinDetailEvent {
    data class LoadCoinDetail(val coinId: String) : CoinDetailEvent()
} 