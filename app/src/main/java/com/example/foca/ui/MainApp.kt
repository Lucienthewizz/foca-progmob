package com.example.foca.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foca.ui.components.BottomNavigationBar
import com.example.foca.ui.nav.AppNavHost
import com.example.foca.ui.nav.Routes

@Composable
fun MainApp(onGoogleSignIn: (() -> Unit)? = null, userId: String? = null, onLogout: (() -> Unit)? = null) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tampilkan BottomBar hanya jika bukan di Loading, Login, SignUp, Admin, atau AdminChat
    val showBottomBar = currentRoute != Routes.LOADING &&
                        currentRoute != Routes.LOGIN &&
                        currentRoute != Routes.SIGNUP &&
                        currentRoute != Routes.ADMIN &&
                        currentRoute?.startsWith("admin_chat") != true

    Column(modifier = Modifier.fillMaxSize()) {
        // Isi utama
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AppNavHost(
                navController = navController,
                onGoogleSignIn = onGoogleSignIn,
                userId = userId,
                onLogout = onLogout
            )
        }

        // Bottom navigation bar
        if (showBottomBar && !userId.isNullOrEmpty()) {
            BottomNavigationBar(navController = navController)
        }
    }
}
