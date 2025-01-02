package com.cursorcoin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cursorcoin.presentation.screens.coindetail.CoinDetailScreen
import com.cursorcoin.presentation.screens.coins.CoinsScreen
import com.cursorcoin.presentation.screens.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Coins.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Coins.route) {
            CoinsScreen(navController)
        }
        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(
                navArgument("coinId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: return@composable
            CoinDetailScreen(navController, coinId)
        }
    }
} 