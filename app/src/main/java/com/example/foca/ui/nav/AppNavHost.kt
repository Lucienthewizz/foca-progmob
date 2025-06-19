package com.example.foca.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foca.ui.screen.CartScreen
import com.example.foca.ui.screen.CateringDailyScreen
import com.example.foca.ui.screen.CateringEventScreen
import com.example.foca.ui.screen.ChatScreen
import com.example.foca.ui.screen.HomeScreen
import com.example.foca.ui.screen.LoadingScreen
import com.example.foca.ui.screen.LoginScreen
import com.example.foca.ui.screen.ProfileScreen
import com.example.foca.ui.screen.SignUpScreen


object Routes {
    const val SIGNUP = "signup"
    const val LOGIN = "login"
    const val LOADING = "loading"
    const val HOME = "home"
    const val CATERING_DAILY = "catering_daily"
    const val CATERING_EVENT = "catering_event"
    const val CART = "cart"
    const val CHAT = "chat"
    const val PROFILE = "profile"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.LOADING) {
        composable(Routes.LOADING) { LoadingScreen(navController) }
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.CATERING_DAILY) { CateringDailyScreen(navController) }
        composable(Routes.CATERING_EVENT) { CateringEventScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController) }
        composable(Routes.CART) { CartScreen(navController) }
        composable(Routes.CHAT) { ChatScreen(navController) }
        composable(Routes.PROFILE) { ProfileScreen(navController) }
        composable(Routes.SIGNUP) { SignUpScreen(navController) }


        // -------- BottomNav items ---------

        // Home(Halaman Utama)
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController)
        }
        // Cart(Keranjang)
        composable(BottomNavItem.Cart.route) {
            CartScreen(navController)
        }
        // Chat(Pesan)
        composable(BottomNavItem.Chat.route) {
            ChatScreen(navController)
        }
        // Profile(Profil)
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
