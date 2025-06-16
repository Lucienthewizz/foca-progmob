package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background // <- penting!
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.nav.Routes
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController) {
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

