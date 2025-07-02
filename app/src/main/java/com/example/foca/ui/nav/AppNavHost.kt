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
import com.example.foca.ui.screen.DetailScreen
import com.example.foca.data.model.CateringItem
import com.google.gson.Gson
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.foca.data.viewmodel.ProfileViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.foca.ui.screen.PaymentScreen
import com.example.foca.ui.screen.OrderHistoryScreen
import com.example.foca.data.viewmodel.CateringViewModel


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
fun AppNavHost(navController: NavHostController, onGoogleSignIn: (() -> Unit)? = null, userId: String? = null) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState = profileViewModel.profile.collectAsState()
    if (!userId.isNullOrEmpty()) {
        LaunchedEffect(userId) { profileViewModel.loadProfile(userId) }
    }
    val userName = profileState.value?.name
    val userPhotoUrl = profileState.value?.photoUrl
    NavHost(navController, startDestination = Routes.LOADING) {
        composable(Routes.LOADING) { LoadingScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController, onGoogleSignIn, userId) }
        composable(Routes.SIGNUP) { SignUpScreen(navController) }

        // -------- Main Routes ---------
        // Menggunakan BottomNavItem.Home.route untuk menghindari duplikasi
        composable(BottomNavItem.Home.route) { HomeScreen(navController, userName, userPhotoUrl) }
        composable(Routes.CATERING_DAILY) { CateringDailyScreen(navController) }
        composable(Routes.CATERING_EVENT) { CateringEventScreen(navController) }
        composable(BottomNavItem.Cart.route) { CartScreen(navController) }
        composable(BottomNavItem.Chat.route) { ChatScreen(navController) }
        composable(BottomNavItem.Profile.route) { ProfileScreen(navController, userId) }

        // New composable route for DetailScreen
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val cateringViewModel: CateringViewModel = viewModel()
            val allItems = cateringViewModel.allItems.collectAsState().value
            androidx.compose.runtime.LaunchedEffect(itemId) {
                if (allItems.isEmpty()) cateringViewModel.loadAllCateringItems()
            }
            val item = allItems.find { it.id == itemId }
            if (item != null) {
                DetailScreen(item = item)
            } else {
                // Optional: tampilkan loading atau pesan data tidak ditemukan
                androidx.compose.material3.Text("Loading menu detail...")
            }
        }

        // New composable route for PaymentScreen
        composable("payment/{address}/{note}/{date}/{total}",
            arguments = listOf(
                navArgument("address") { type = NavType.StringType },
                navArgument("note") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address") ?: ""
            val note = backStackEntry.arguments?.getString("note") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val total = backStackEntry.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
            PaymentScreen(address, note, date, total, onConfirm = {}, navController = navController)
        }
        composable("home?paymentSuccess={paymentSuccess}", arguments = listOf(
            navArgument("paymentSuccess") { type = NavType.StringType; defaultValue = "false" }
        )) { backStackEntry ->
            val paymentSuccess = backStackEntry.arguments?.getString("paymentSuccess") == "true"
            HomeScreen(navController, userName, userPhotoUrl, paymentSuccess)
        }
        composable("order_history") {
            OrderHistoryScreen(navController, userId)
        }
    }
}
