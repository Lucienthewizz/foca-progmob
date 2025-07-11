package com.example.foca.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.example.foca.ui.screen.AdminScreen
import com.example.foca.ui.screen.AdminChatScreen
import android.util.Log


object Routes {
    const val SIGNUP = "signup"
    const val LOGIN = "login"
    const val LOADING = "loading"
    const val HOME = "home"
    const val ADMIN = "admin"
    const val CATERING_DAILY = "catering_daily"
    const val CATERING_EVENT = "catering_event"
    const val CART = "cart"
    const val CHAT = "chat"
    const val PROFILE = "profile"
    const val ADMIN_CHAT = "admin_chat/{userId}"
}

@Composable
fun AppNavHost(navController: NavHostController, onGoogleSignIn: (() -> Unit)? = null, userId: String? = null, onLogout: (() -> Unit)? = null) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState = profileViewModel.profile.collectAsState()
    if (!userId.isNullOrEmpty()) {
        LaunchedEffect(userId) { profileViewModel.loadProfile(userId) }
    }
    val userName = profileState.value?.firstName?.ifEmpty { 
        profileState.value?.name?.split(" ")?.firstOrNull() 
    } ?: "User"
    val userPhotoUrl = profileState.value?.photoUrl
    
    // Check if user is admin
    val isAdmin = profileState.value?.role == "admin"
    val currentProfile = profileState.value
    
    // Handle navigation based on user role and authentication
    LaunchedEffect(userId, currentProfile) {
        val currentRoute = navController.currentDestination?.route
        Log.d("AppNavHost", "Navigation check - userId: $userId, profile: ${currentProfile?.email}, role: ${currentProfile?.role}, currentRoute: $currentRoute")
        
        if (userId.isNullOrEmpty()) {
            // User logged out, go to login
            Log.d("AppNavHost", "User logged out, navigating to login")
            if (currentRoute != Routes.LOGIN && currentRoute != Routes.LOADING && currentRoute != Routes.SIGNUP) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else if (currentProfile != null) {
            // User logged in and profile loaded, check role
            val userRole = currentProfile.role ?: "user"
            Log.d("AppNavHost", "User logged in with role: $userRole")
            if (userRole == "admin" && currentRoute != Routes.ADMIN) {
                Log.d("AppNavHost", "Navigating admin to AdminScreen")
                navController.navigate(Routes.ADMIN) {
                    popUpTo(0) { inclusive = true }
                }
            } else if (userRole == "user" && (currentRoute == Routes.ADMIN || currentRoute == Routes.LOGIN || currentRoute == Routes.SIGNUP || currentRoute == Routes.LOADING)) {
                Log.d("AppNavHost", "Navigating user to HomeScreen")
                navController.navigate(BottomNavItem.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
    
    // Always start with loading screen
    val startDestination = Routes.LOADING
    
    NavHost(navController, startDestination = startDestination) {
        composable(Routes.LOADING) { LoadingScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController, onGoogleSignIn, userId) }
        composable(Routes.SIGNUP) { SignUpScreen(navController) }
        composable(Routes.ADMIN) { AdminScreen(navController, userId, onLogout) }

        // -------- Main Routes ---------
        // Menggunakan BottomNavItem.Home.route untuk menghindari duplikasi
        composable(BottomNavItem.Home.route) { HomeScreen(navController, userName, userPhotoUrl) }
        composable(Routes.CATERING_DAILY) { CateringDailyScreen(navController) }
        composable(Routes.CATERING_EVENT) { CateringEventScreen(navController) }
        composable(BottomNavItem.Cart.route) { CartScreen(navController) }
        composable(BottomNavItem.Chat.route) {
            if (userId != null) {
                ChatScreen(navController, userId = userId)
            }
        }
        composable(BottomNavItem.Profile.route) { ProfileScreen(navController, userId, onLogout) }

        // New composable route for DetailScreen with smooth animation
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
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
        composable("payment/{total}",
            arguments = listOf(
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val total = backStackEntry.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
            PaymentScreen(total = total, navController = navController)
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
        composable(
            route = Routes.ADMIN_CHAT,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatUserId = backStackEntry.arguments?.getString("userId")
            if (chatUserId != null) {
                AdminChatScreen(navController = navController, userId = chatUserId)
            } else {
                // Handle error, e.g., navigate back or show an error message
            }
        }
    }
}
