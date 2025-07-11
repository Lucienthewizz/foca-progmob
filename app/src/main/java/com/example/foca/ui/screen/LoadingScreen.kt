package com.example.foca.ui.screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.nav.BottomNavItem
import com.example.foca.ui.nav.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import android.util.Log

@Composable
fun LoadingScreen(navController: NavController) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    SideEffect {
        // ✅ Ini pengganti deprecated: izinkan layout masuk ke area status/nav bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // ✅ Atur warna ikon status/nav bar
        WindowInsetsControllerCompat(window, view).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }

    LaunchedEffect(Unit) {
        // Tampilkan loading screen selama 3 detik
        delay(3000)
        
        // Setelah loading, check session
        val currentUser = firebaseAuth.currentUser
        val sharedPref = context.getSharedPreferences("foca_prefs", Context.MODE_PRIVATE)
        val rememberMe = sharedPref.getBoolean("remember_me", false)
        
        if (currentUser != null && rememberMe) {
            Log.d("LoadingScreen", "User found: ${currentUser.email}, Remember Me: $rememberMe")
            // Check user role from Firestore
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userRole = document.getString("role") ?: "user"
                        val userEmail = document.getString("email") ?: ""
                        Log.d("LoadingScreen", "User role: $userRole, Email: $userEmail")
                        
                        if (userRole == "admin") {
                            Log.d("LoadingScreen", "Navigating to Admin Panel")
                            navController.navigate(Routes.ADMIN) {
                                popUpTo(Routes.LOADING) { inclusive = true }
                            }
                        } else {
                            Log.d("LoadingScreen", "Navigating to Home")
                            navController.navigate(BottomNavItem.Home.route) {
                                popUpTo(Routes.LOADING) { inclusive = true }
                            }
                        }
                    } else {
                        Log.d("LoadingScreen", "User document not found, going to login")
                        // User document not found, go to login
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOADING) { inclusive = true }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("LoadingScreen", "Error fetching user data", exception)
                    // Error fetching user data, go to login
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOADING) { inclusive = true }
                    }
                }
        } else {
            // User belum login atau remember me tidak aktif -> ke Login
            if (currentUser != null) {
                firebaseAuth.signOut() // Sign out jika remember me tidak aktif
            }
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.LOADING) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCB507)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.foco_transparant_colorbg),
            contentDescription = "Logo FOCA",
            modifier = Modifier.size(300.dp)
        )
    }
}

