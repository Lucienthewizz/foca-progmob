package com.example.foca.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foca.ui.nav.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            val selected = if (item == BottomNavItem.Home) {
                currentRoute?.startsWith(item.route) == true
            } else {
                currentRoute == item.route
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        // Warna Icon saat Selected
                        tint = if (selected) Color(0xFFFCB507) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        // Warna Text saat Selected
                        color = if (selected) Color(0xFFFCB507) else Color.Gray
                    )
                },
                alwaysShowLabel = true,

                // Warna BG saat Selected
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFFFF3CD)

            ))
        }
    }
}
