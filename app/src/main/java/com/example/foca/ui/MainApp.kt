package com.example.foca.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foca.ui.components.BottomNavigationBar
import com.example.foca.ui.nav.AppNavHost
import com.example.foca.ui.nav.Routes

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tampilkan BottomBar hanya jika bukan di Loading atau Login
    val showBottomBar = currentRoute != Routes.LOADING && currentRoute != Routes.LOGIN

    Column(modifier = Modifier.fillMaxSize()) {
        // Isi utama
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AppNavHost(navController = navController)
        }

        // Bottom navigation bar
        if (showBottomBar) {
            BottomNavigationBar(navController = navController)
        }
    }
}
