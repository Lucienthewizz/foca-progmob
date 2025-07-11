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
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


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
    val startDestination = if (userId.isNullOrEmpty()) Routes.LOADING else Routes.HOME
    
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

        // New composable route for DetailScreen with smooth animation
        composable(
            "detail/{itemJson}",
            arguments = listOf(navArgument("itemJson") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) { backStackEntry ->
            val itemJson = backStackEntry.arguments?.getString("itemJson")
            if (itemJson.isNullOrEmpty()) {
                // Jika data tidak valid, kembali ke layar sebelumnya
                navController.popBackStack()
                return@composable
            }
            val item = try {
                val decodedItemJson = URLDecoder.decode(itemJson, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedItemJson, CateringItem::class.java)
            } catch (e: Exception) {
                // Jika parsing gagal, kembali ke layar sebelumnya
                Log.e("AppNavHost", "Error parsing itemJson: $itemJson", e)
                navController.popBackStack()
                return@composable
            }
            val ownerProfileState = profileViewModel.userProfile.collectAsState()
            LaunchedEffect(item.userId) {
                if (!item.userId.isNullOrEmpty()) {
                    profileViewModel.loadUserProfile(item.userId)
                }
            }
            DetailScreen(navController = navController, item = item, owner = if (!item.userId.isNullOrEmpty()) ownerProfileState.value else null)
        }
        composable(
            Routes.CART,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            val cateringViewModel: CateringViewModel = viewModel()
            CartScreen(navController = navController, cateringViewModel = cateringViewModel)
        }
        composable(
            Routes.CHAT,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            if (userId != null) {
                ChatScreen(navController, userId = userId)
            }
        }
        composable(
            Routes.PROFILE,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            if (userId != null) {
                ProfileScreen(navController, userId, onLogout)
            }
        }
        composable(
            route = "payment/{total}",
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
