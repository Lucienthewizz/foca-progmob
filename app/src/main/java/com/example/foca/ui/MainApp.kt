package com.example.foca.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.foca.ui.nav.AppNavHost

@Composable
fun MainApp() {
    val navController = rememberNavController()
    AppNavHost(navController)
} 