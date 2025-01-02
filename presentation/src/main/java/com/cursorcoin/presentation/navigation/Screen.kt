package com.cursorcoin.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    // Add more screens here
    
    companion object {
        fun fromRoute(route: String?): Screen {
            return when(route) {
                Home.route -> Home
                else -> Home
            }
        }
    }
} 