package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foca.data.viewmodel.ProfileViewModel
import com.example.foca.ui.theme.PoppinsFont
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import com.example.foca.R

@Composable
fun SimpleStatsItem(
    icon: ImageVector,
    count: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count,
            fontFamily = PoppinsFont,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
    }
}

@Composable
fun SimpleProfileMenuItem(
    icon: ImageVector,
    text: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFCB507),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontFamily = PoppinsFont,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF666666),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EnhancedProfileMenuItem(
    icon: ImageVector,
    text: String,
    subtitle: String? = null,
    textColor: Color = Color(0xFF1A1A1A),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontFamily = PoppinsFont,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF666666),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController, userId: String? = null, onLogout: (() -> Unit)? = null) {
    val viewModel: ProfileViewModel = viewModel()
    val uid = userId ?: ""
    val profileState = viewModel.profile.collectAsState()
    val ordersState = viewModel.orders.collectAsState()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    // Edit profile form states
    var editName by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            viewModel.loadProfile(uid)
            viewModel.loadOrders(uid)
        }
    }

    val profile = profileState.value
    val orders = ordersState.value

    // Inisialisasi GoogleSignInClient
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAF8F0),
                        Color(0xFFF8F9FA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simplified Header with cleaner profile section  
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Reduced height for simpler layout
            ) {
                // Flatter background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Reduced gradient area
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFCB507),
                                    Color(0xFFFFD54F)
                                )
                            )
                        )
                )
                
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Profil",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White,
                        letterSpacing = 0.3.sp
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    // Simple Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.White, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile != null && !profile.photoUrl.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(profile.photoUrl),
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_john),
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            
            // Simplified Profile Info Section - No Card, flatter design
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-50).dp), // Less overlap for a cleaner look
                horizontalAlignment = Alignment.CenterHorizontally
            ) {                
                if (profile != null) {
                    Text(
                        text = profile.name,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF1A1A1A),
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = profile.email,
                            fontFamily = PoppinsFont,
                            fontSize = 15.sp,
                            color = Color(0xFF666666)
                        )
                    }
                } else {
                    Text(
                        text = "John Doe",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF1A1A1A),
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "john.doe@example.com",
                            fontFamily = PoppinsFont,
                            fontSize = 15.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Simple Stats Row with minimal design
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleStatsItem(
                        icon = Icons.Default.ShoppingCart,
                        count = "${orders.size}",
                        label = "Pesanan",
                        color = Color(0xFF4CAF50)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFE0E0E0))
                    )
                    SimpleStatsItem(
                        icon = Icons.Default.Star,
                        count = "4.8",
                        label = "Rating",
                        color = Color(0xFFFF9800)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFFE0E0E0))
                    )
                    SimpleStatsItem(
                        icon = Icons.Default.Receipt,
                        count = "${orders.size}",
                        label = "Transaksi",
                        color = Color(0xFF2196F3)
                    )
                }
            }
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            Spacer(modifier = Modifier.height(28.dp))
            
            // Enhanced Menu Items with improved spacing
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)

            ) {
                // Quick Actions dengan design simple
                Text(
                    text = "Aksi Cepat",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                SimpleProfileMenuItem(
                    icon = Icons.Filled.History,
                    text = "Riwayat Pesanan",
                    subtitle = "Lihat pesanan sebelumnya",
                    onClick = { navController.navigate("order_history") }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SimpleProfileMenuItem(
                    icon = Icons.Filled.Edit,
                    text = "Edit Profil",
                    subtitle = "Perbarui informasi Anda",
                    onClick = { 
                        showEditDialog = true
                    }
                )
                
                Spacer(modifier = Modifier.height(28.dp)) // Increased spacing between sections
                
                // Account Settings with better typography
                Text(
                    text = "Akun",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                EnhancedProfileMenuItem(
                    icon = Icons.Filled.ExitToApp,
                    text = "Keluar",
                    subtitle = "Keluar dari akun Anda",
                    textColor = Color(0xFFE53E3E),
                    onClick = { showLogoutDialog = true }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp)) // Increased bottom spacing
        }

    // All dialog and state logic below remains inside the composable
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { 
                Text(
                    "Konfirmasi Logout",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = { 
                Text(
                    "Apakah Anda yakin ingin logout?",
                    fontFamily = PoppinsFont
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    FirebaseAuth.getInstance().signOut()
                    googleSignInClient.signOut().addOnCompleteListener {
                        onLogout?.invoke()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }) {
                    Text(
                        "Ya",
                        fontFamily = PoppinsFont,
                        color = Color(0xFFE53E3E)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        "Tidak",
                        fontFamily = PoppinsFont
                    )
                }
            }
        )
    }
    
    // Edit Profile Dialog
    if (showEditDialog) {
        LaunchedEffect(showEditDialog) {
            if (profile != null) {
                editName = profile.name
                editEmail = profile.email
                editPhone = profile.phone ?: ""
            }
        }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    "Edit Profil",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nama", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Telepon", fontFamily = PoppinsFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (uid.isNotEmpty()) {
                        viewModel.updateProfile(
                            uid = uid,
                            name = editName,
                            email = editEmail,
                            phone = editPhone
                        )
                    }
                    showEditDialog = false
                }) {
                    Text(
                        "Simpan",
                        fontFamily = PoppinsFont,
                        color = Color(0xFFFCB507)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(
                        "Batal",
                        fontFamily = PoppinsFont
                    )
                }
            }
        )
    }
}}