package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foca.data.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController, userId: String? = null) {
    val viewModel: ProfileViewModel = viewModel()
    val uid = userId ?: ""
    val profileState = viewModel.profile.collectAsState()
    val ordersState = viewModel.orders.collectAsState()

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            viewModel.loadProfile(uid)
            viewModel.loadOrders(uid)
        }
    }

    val profile = profileState.value
    val orders = ordersState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 85.dp, bottom = 30.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("My Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
        }
        // Avatar, Name, Email
        Spacer(modifier = Modifier.height(8.dp))
        if (profile != null && profile.photoUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(profile.photoUrl),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color(0xFFFCB507), CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = com.example.foca.R.drawable.ic_john),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color(0xFFFCB507), CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (profile != null) {
            Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Text(profile.email, fontSize = 14.sp, color = Color.Gray)
        }
        // Tombol ke Order History
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { navController.navigate("order_history") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Icon(Icons.Filled.History, contentDescription = null, tint = Color(0xFFFCB507))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Order History", color = Color(0xFFFCB507), fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        // About
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Text("About", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black)
            Text("-", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
        }
        // Menu List
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            ProfileMenuItem(icon = Icons.Filled.Edit, text = "Edit Profile", onClick = { /* TODO: Edit profile */ })
            ProfileMenuItem(icon = Icons.Filled.Bookmark, text = "Saved", onClick = { /* TODO: Saved */ })
            Divider(modifier = Modifier.padding(vertical = 2.dp))
            ProfileMenuItem(icon = Icons.Filled.ExitToApp, text = "Log Out", textColor = Color.Red, onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") { popUpTo(0) { inclusive = true } }
            })
        }
        // Riwayat Pemesanan
        Spacer(modifier = Modifier.height(18.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Text("Riwayat Pemesanan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            if (orders.isEmpty()) {
                Text("Belum ada riwayat pesanan.", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
            } else {
                orders.sortedByDescending { it.date }.take(2).forEach { order ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Tanggal: ${order.date}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Alamat: ${order.address}", fontSize = 13.sp, maxLines = 1)
                            Text("Total: Rp ${order.total}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Status: ${order.status}", color = Color(0xFFFCB507), fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
                TextButton(onClick = { navController.navigate("order_history") }, modifier = Modifier.align(Alignment.End)) {
                    Text("Lihat Semua", color = Color(0xFF6DC36D), fontSize = 13.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, textColor: Color = Color.Black, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}