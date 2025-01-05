package com.cursorcoin.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cursorcoin.presentation.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        NavigationItem(
            name = "Coins",
            route = Screen.CoinList.route,
            icon = Icons.Default.List
        ),
        NavigationItem(
            name = "Portfolio",
            route = Screen.Portfolio.route,
            icon = Icons.Default.Lock
        ),
        NavigationItem(
            name = "Market",
            route = Screen.MarketAnalysis.route,
            icon = Icons.Default.DateRange
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Only show BottomBar if the current destination is one of our main screens
    if (items.any { it.route == currentRoute }) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.name) },
                    label = { Text(text = item.name) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
} 