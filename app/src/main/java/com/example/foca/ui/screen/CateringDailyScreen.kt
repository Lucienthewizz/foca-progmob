package com.example.foca.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CateringDailyScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        Text("Catering Daily", style = MaterialTheme.typography.titleLarge)
        // Add your daily catering content here
    }
} 