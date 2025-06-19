package com.example.foca.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background // <- penting!
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.nav.Routes
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController) {
    val view = LocalView.current
    val window = (view.context as Activity).window

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
        delay(3000)
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.LOADING) { inclusive = true } // Menghapus dari backstack
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

