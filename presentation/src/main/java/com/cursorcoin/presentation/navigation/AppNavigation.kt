package com.cursorcoin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cursorcoin.presentation.screens.coindetail.CoinDetailScreen
import com.cursorcoin.presentation.screens.coins.CoinsScreen
import com.cursorcoin.presentation.screens.portfolio.PortfolioScreen
import com.cursorcoin.presentation.screens.settings.SettingsScreen
import com.cursorcoin.presentation.screens.market.MarketAnalysisScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.CoinList.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.CoinList.route) {
            CoinsScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToDetail = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                },
                navController = navController
            )
        }
        composable(Screen.Portfolio.route) {
            PortfolioScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(Screen.MarketAnalysis.route) {
            MarketAnalysisScreen()
        }
        composable(Screen.CoinDetail.route) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: return@composable
            CoinDetailScreen(
                navController = navController,
                coinId = coinId
            )
        }
    }
} 