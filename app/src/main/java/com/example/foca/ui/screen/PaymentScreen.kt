package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.ui.theme.PoppinsFont
import com.example.foca.R
import com.example.foca.data.model.Order
import com.example.foca.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.navigation.NavController

@Composable
fun PaymentScreen(
    address: String,
    note: String,
    date: String,
    total: Double,
    onConfirm: () -> Unit,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val repository = remember { FirebaseRepository() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pembayaran", fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = PoppinsFont)
        Spacer(modifier = Modifier.height(24.dp))

        // Metode Pembayaran
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook), // Ganti dengan gambar QRIS jika ada
                    contentDescription = "QRIS",
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text("QRIS", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 18.sp)
                    Text("Scan untuk membayar dengan QRIS", fontFamily = PoppinsFont, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Ringkasan
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Ringkasan Pesanan", fontWeight = FontWeight.Medium, fontFamily = PoppinsFont, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Alamat", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
                Text(address, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Catatan", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
                Text(note, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tanggal", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont)
                Text(date, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 16.sp)
                    Text("Rp $total", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 16.sp, color = Color(0xFFFCB507))
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                if (userId.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        val order = Order(
                            userId = userId,
                            address = address,
                            note = note,
                            date = date,
                            total = total,
                            paymentMethod = "QRIS",
                            status = "Sukses"
                        )
                        repository.saveOrder(order)
                        isLoading = false
                        // Redirect ke Home dengan flag paymentSuccess
                        navController.navigate("home?paymentSuccess=true") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Konfirmasi Pembayaran", fontFamily = PoppinsFont, fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
        }
    }
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false; onConfirm() },
            title = { Text("Pembayaran Berhasil!", fontFamily = PoppinsFont, fontWeight = FontWeight.Bold) },
            text = { Text("Terima kasih, pesanan Anda telah dicatat. Silakan cek riwayat di profil.", fontFamily = PoppinsFont) },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false; onConfirm() }) {
                    Text("OK", fontFamily = PoppinsFont)
                }
            }
        )
    }
} 