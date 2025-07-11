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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.data.model.Order
import com.example.foca.data.repository.FirebaseRepository
import com.example.foca.data.viewmodel.ProfileViewModel
import com.example.foca.ui.theme.PoppinsFont
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PaymentScreen(
    total: Double,
    navController: NavController
) {
    // Data class for bank options
    data class BankOption(val name: String, val logo: Int, val account: String)
    val coroutineScope = rememberCoroutineScope()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val repository = remember { FirebaseRepository() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    
    // Get user profile
    val profileViewModel: ProfileViewModel = viewModel()
    val userProfile by profileViewModel.profile.collectAsState()
    val senderName = userProfile?.firstName ?: "User"
    
    // Load profile if user is logged in
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            profileViewModel.loadProfile(userId)
        }
    }
    
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Bank Transfer") }
    var showError by remember { mutableStateOf(false) }
    val bankOptions: List<BankOption> = listOf(
        BankOption("BCA", R.drawable.bca, "1234567890 a.n. PT Foca Indonesia"),
        BankOption("BNI", R.drawable.bni, "9876543210 a.n. PT Foca Indonesia")
    )
    val paymentOptions: List<String> = listOf("BCA", "BNI", "QRIS")
    var expanded by remember { mutableStateOf(false) }
    var selectedBank: BankOption by remember { mutableStateOf(bankOptions[0]) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp), // space for floating button
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simple Header with minimal design
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 60.dp, bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Pembayaran",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        color = Color(0xFF1A1A1A),
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Lengkapi detail pembayaran Anda",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFont,
                        color = Color(0xFF666666)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Informasi Pengiriman Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Simple Section Header with Step Number
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFFCB507), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "1",
                                fontWeight = FontWeight.Bold,
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Informasi Pengiriman", 
                            fontWeight = FontWeight.Bold, 
                            fontFamily = PoppinsFont, 
                            fontSize = 18.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                }
                
                // Simplified form fields
                Text(
                    "Alamat Pengiriman", 
                    fontWeight = FontWeight.Medium, 
                    fontFamily = PoppinsFont, 
                    fontSize = 15.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(top = 16.dp)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("Masukkan alamat lengkap", fontFamily = PoppinsFont, color = Color(0xFF999999)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFCB507),
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                
                Text(
                    "Catatan (opsional)", 
                    fontWeight = FontWeight.Medium, 
                    fontFamily = PoppinsFont, 
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = { Text("Catatan untuk kurir/katering", fontFamily = PoppinsFont, color = Color(0xFF999999)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    singleLine = false,
                    maxLines = 2,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFCB507),
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                
                Text(
                    "Tanggal Pengiriman", 
                    fontWeight = FontWeight.Medium, 
                    fontFamily = PoppinsFont, 
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    placeholder = { Text("yyyy-MM-dd", fontFamily = PoppinsFont, color = Color(0xFF999999)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFCB507),
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Metode Pembayaran Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Simple Section Header with Step Number
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFFCB507), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "2",
                                fontWeight = FontWeight.Bold,
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Metode Pembayaran", 
                            fontWeight = FontWeight.Bold, 
                            fontFamily = PoppinsFont, 
                            fontSize = 18.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                }
                
                // Simple Dropdown Selection
                Text(
                    "Pilih Metode Pembayaran", 
                    fontWeight = FontWeight.Medium, 
                    fontFamily = PoppinsFont, 
                    fontSize = 15.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Box(modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)) {
                    OutlinedTextField(
                        value = paymentMethod,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Pilih metode pembayaran", fontFamily = PoppinsFont, color = Color(0xFF999999)) },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color(0xFF999999))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFCB507),
                            unfocusedBorderColor = Color(0xFFEEEEEE),
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
                                            Image(painterResource(id = R.drawable.bca), contentDescription = "BCA", modifier = Modifier.size(32.dp))
                                            Spacer(Modifier.width(12.dp))
                                        } else if (option == "BNI") {
                                            Image(painterResource(id = R.drawable.bni), contentDescription = "BNI", modifier = Modifier.size(32.dp))
                                            Spacer(Modifier.width(12.dp))
                                        } else if (option == "QRIS") {
                                            Image(painterResource(id = R.drawable.qris), contentDescription = "QRIS", modifier = Modifier.size(32.dp))
                                            Spacer(Modifier.width(12.dp))
                                        }
                                        Text(option, fontFamily = PoppinsFont, fontSize = 16.sp)
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
                
                // Simplified Payment Method Display
                if (paymentMethod == "QRIS") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.qris),
                            contentDescription = "QRIS",
                            modifier = Modifier.size(140.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Scan QRIS untuk membayar", 
                            fontFamily = PoppinsFont, 
                            color = Color(0xFF666666), 
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                } else {
                    val selectedBank = bankOptions.find { it.name == paymentMethod }
                    if (selectedBank != null) {
                        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painterResource(id = selectedBank.logo), 
                                    contentDescription = selectedBank.name, 
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    selectedBank.name, 
                                    fontFamily = PoppinsFont, 
                                    fontWeight = FontWeight.Medium, 
                                    fontSize = 16.sp,
                                    color = Color(0xFF1A1A1A)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                selectedBank.account, 
                                fontFamily = PoppinsFont, 
                                fontWeight = FontWeight.Medium, 
                                fontSize = 15.sp,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Ringkasan Pesanan Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Simple Section Header with Step Number
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFFCB507), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "3",
                                fontWeight = FontWeight.Bold,
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Ringkasan Pesanan", 
                            fontWeight = FontWeight.Bold, 
                            fontFamily = PoppinsFont, 
                            fontSize = 18.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                }
                
                // Summary Items with minimal styling
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    SummaryItem("Alamat", address.ifEmpty { "Belum diisi" })
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryItem("Catatan", note.ifEmpty { "Tidak ada" })
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryItem("Tanggal", date.ifEmpty { "Belum diisi" })
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryItem("Metode Pembayaran", paymentMethod)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Simple total section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = "Total Pembayaran",
                            fontWeight = FontWeight.Medium,
                            fontFamily = PoppinsFont,
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Rp ${String.format("%,.0f", total)}",
                            fontFamily = PoppinsFont,
                            fontSize = 20.sp,
                            color = Color(0xFFFCB507),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "⚠️",
                        fontSize = 18.sp,
                        color = Color(0xFFD32F2F)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Alamat dan tanggal pengiriman wajib diisi",
                        color = Color(0xFFD32F2F),
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp
                    )
                }
            }
        }
        // Place the bottom button using BoxScope
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (userId.isNotBlank() && address.isNotBlank() && date.isNotBlank()) {
                            isLoading = true
                            showError = false
                            coroutineScope.launch {
                                try {
                                    val order = Order(
                                        userId = userId,
                                        senderName = senderName,
                                        address = address,
                                        note = note,
                                        date = date,
                                        total = total,
                                        paymentMethod = paymentMethod,
                                        status = "pending"
                                    )
                                    repository.saveOrder(order)
                                    isLoading = false
                                    showSuccessDialog = true
                                } catch (e: Exception) {
                                    isLoading = false
                                    showError = true
                                }
                            }
                        } else {
                            showError = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCB507),
                        disabledContainerColor = Color(0xFFFCB507).copy(alpha = 0.6f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = Color.White, 
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Memproses...",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Konfirmasi Pembayaran",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
        if (showSuccessDialog) {
            // Custom Success Dialog dengan positioning yang lebih baik
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(enabled = false) { }
                    .systemBarsPadding(), // Tambahkan ini untuk menghindari system bars
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Success Icon
                        Card(
                            modifier = Modifier.size(96.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507).copy(alpha = 0.1f)),
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFFFCB507),
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(28.dp))
                        
                        // Title
                        Text(
                            text = "Pembayaran Berhasil!",
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF1A1A1A),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Description
                        Text(
                            text = "Terima kasih! Pesanan Anda telah berhasil diproses. Silakan cek riwayat pesanan di profil Anda.",
                            fontFamily = PoppinsFont,
                            fontSize = 16.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(36.dp))
                        
                        // Button
                        Button(
                            onClick = { 
                                showSuccessDialog = false
                                navController.navigate("home") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Kembali ke Beranda",
                                    fontFamily = PoppinsFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 15.sp,
            color = Color(0xFF666666)
        )
        Text(
            text = value,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color(0xFF1A1A1A)
        )
    }
}