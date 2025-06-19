package com.example.foca.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Cart : BottomNavItem("cart", "Keranjang", Icons.Filled.ShoppingCart)
    object Chat : BottomNavItem("chat", "Chat", Icons.AutoMirrored.Filled.Message)
    object Profile : BottomNavItem("profile", "Profil", Icons.Filled.Person)
}