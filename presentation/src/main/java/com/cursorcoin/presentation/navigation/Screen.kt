package com.cursorcoin.presentation.navigation

sealed class Screen(val route: String) {
    object CoinList : Screen("coin_list")
    object Portfolio : Screen("portfolio")
    object Settings : Screen("settings")
    object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String) = "coin_detail/$coinId"
    }
} 