package com.example.foca.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foca.ui.screen.CateringDailyScreen
import com.example.foca.ui.screen.CateringEventScreen
import com.example.foca.ui.screen.HomeScreen

object Routes {
    const val HOME = "home"
    const val CATERING_DAILY = "catering_daily"
    const val CATERING_EVENT = "catering_event"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.CATERING_DAILY) { CateringDailyScreen(navController) }
        composable(Routes.CATERING_EVENT) { CateringEventScreen(navController) }
    }
} 