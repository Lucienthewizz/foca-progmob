package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.data.model.Order
import com.example.foca.data.repository.FirebaseRepository
import com.example.foca.ui.theme.PoppinsFont
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(
    total: Double,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val repository = remember { FirebaseRepository() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Bank Transfer") }
    var showError by remember { mutableStateOf(false) }
    val bankOptions = listOf(
        BankOption("BCA", R.drawable.bca, "1234567890 a.n. PT Foca Indonesia"),
        BankOption("BNI", R.drawable.bni, "9876543210 a.n. PT Foca Indonesia")
    )
    val paymentOptions = listOf("BCA", "BNI", "QRIS")
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp), // space for floating button
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 36.dp, bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Pembayaran",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF222222)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Form Input
            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Alamat Pengiriman", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont, fontSize = 16.sp)
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text("Masukkan alamat lengkap", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text("Catatan (opsional)", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont, fontSize = 16.sp)
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text("Catatan untuk kurir/katering", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text("Tanggal Pengiriman", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont, fontSize = 16.sp)
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        placeholder = { Text("yyyy-MM-dd", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            // Metode Pembayaran
            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Metode Pembayaran", fontWeight = FontWeight.SemiBold, fontFamily = PoppinsFont, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    // Dropdown
                    Box {
                        OutlinedTextField(
                            value = paymentMethod,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pilih Metode Pembayaran", fontFamily = PoppinsFont) },
                            trailingIcon = {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        // Overlay clickable area
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Transparent)
                                .clickable { expanded = true }
                        ) {}
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            paymentOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (option == "BCA") {
                                                Image(painterResource(id = R.drawable.bca), contentDescription = "BCA", modifier = Modifier.size(28.dp))
                                                Spacer(Modifier.width(8.dp))
                                            } else if (option == "BNI") {
                                                Image(painterResource(id = R.drawable.bni), contentDescription = "BNI", modifier = Modifier.size(28.dp))
                                                Spacer(Modifier.width(8.dp))
                                            } else if (option == "QRIS") {
                                                Image(painterResource(id = R.drawable.qris), contentDescription = "QRIS", modifier = Modifier.size(28.dp))
                                                Spacer(Modifier.width(8.dp))
                                            }
                                            Text(option, fontFamily = PoppinsFont)
                                        }
                                    },
                                    onClick = {
                                        paymentMethod = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    // Tampilkan info sesuai pilihan
                    if (paymentMethod == "QRIS") {
                        Spacer(modifier = Modifier.height(18.dp))
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.qris),
                                contentDescription = "QRIS",
                                modifier = Modifier.size(160.dp)
                            )
                            Text("Scan QRIS untuk membayar", fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    } else {
                        val selectedBank = bankOptions.find { it.name == paymentMethod }
                        if (selectedBank != null) {
                            Spacer(modifier = Modifier.height(18.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painterResource(id = selectedBank.logo), contentDescription = selectedBank.name, modifier = Modifier.size(36.dp))
                                Spacer(Modifier.width(12.dp))
                                Text(selectedBank.name, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
                            ) {
                                Column(Modifier.padding(14.dp)) {
                                    Text(selectedBank.account, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            // Divider halus
            Divider(color = Color(0xFFF0E9D2), thickness = 1.dp, modifier = Modifier.padding(horizontal = 40.dp))
            Spacer(modifier = Modifier.height(22.dp))
            // Ringkasan
            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Ringkasan Pesanan", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Alamat", fontWeight = FontWeight.Medium, fontFamily = PoppinsFont)
                        Text(address, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp, maxLines = 1)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Catatan", fontWeight = FontWeight.Medium, fontFamily = PoppinsFont)
                        Text(note, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp, maxLines = 1)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Tanggal", fontWeight = FontWeight.Medium, fontFamily = PoppinsFont)
                        Text(date, fontFamily = PoppinsFont, color = Color.Gray, fontSize = 14.sp, maxLines = 1)
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 18.sp)
                        Text("Rp $total", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 18.sp, color = Color(0xFFFCB507))
                    }
                }
            }
            if (showError) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Alamat dan tanggal wajib diisi!", color = Color.Red, fontFamily = PoppinsFont, fontSize = 15.sp)
            }
        }
        // Floating Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (userId.isNotBlank() && address.isNotBlank() && date.isNotBlank()) {
                        isLoading = true
                        showError = false
                        coroutineScope.launch {
                            val order = Order(
                                userId = userId,
                                address = address,
                                note = note,
                                date = date,
                                total = total,
                                paymentMethod = paymentMethod,
                                status = "Sukses"
                            )
                            repository.saveOrder(order)
                            isLoading = false
                            navController.navigate("home?paymentSuccess=true") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    } else {
                        showError = true
                    }
                },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Konfirmasi Pembayaran", fontFamily = PoppinsFont, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Pembayaran Berhasil!", fontFamily = PoppinsFont, fontWeight = FontWeight.Bold) },
                text = { Text("Terima kasih, pesanan Anda telah dicatat. Silakan cek riwayat di profil.", fontFamily = PoppinsFont) },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("OK", fontFamily = PoppinsFont)
                    }
                }
            )
        }
    }
}

// Tambahkan data bank
data class BankOption(val name: String, val logo: Int, val account: String) 