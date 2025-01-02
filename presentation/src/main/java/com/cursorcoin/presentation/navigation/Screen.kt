package com.cursorcoin.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Coins : Screen("coins")
    object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String) = "coin_detail/$coinId"
    }
    
    companion object {
        fun fromRoute(route: String?): Screen {
            return when(route?.substringBefore("/")) {
                Home.route -> Home
                Coins.route -> Coins
                "coin_detail" -> CoinDetail
                else -> Home
            }
        }
    }
} 