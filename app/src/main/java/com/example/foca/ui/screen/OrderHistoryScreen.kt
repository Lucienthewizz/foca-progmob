package com.example.foca.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foca.data.viewmodel.ProfileViewModel
import com.example.foca.data.model.Order
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun OrderHistoryScreen(navController: NavController, userId: String?) {
    val viewModel: ProfileViewModel = viewModel()
    val orders = viewModel.orders.collectAsState().value
    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) viewModel.loadOrders(userId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Riwayat Pemesanan", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (orders.isEmpty()) {
            Text("Belum ada riwayat pesanan.", color = Color.Gray, fontSize = 14.sp)
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                orders.sortedByDescending { it.date }.forEach { order ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tanggal: ${order.date}", fontWeight = FontWeight.Bold)
                            Text("Alamat: ${order.address}", fontSize = 14.sp)
                            Text("Catatan: ${order.note}", fontSize = 14.sp)
                            Text("Total: Rp ${order.total}", fontWeight = FontWeight.SemiBold)
                            Text("Status: ${order.status}", color = Color(0xFFFCB507), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
} 