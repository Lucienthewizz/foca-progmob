package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.nav.Routes
import com.example.foca.ui.theme.PoppinsFont
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navController: NavController, onGoogleSignIn: (() -> Unit)? = null, userId: String? = null) {
    val context = LocalContext.current
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Logo/Brand
        Image(
            painter = painterResource(id = R.drawable.foco_transparant),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Selamat Datang Kembali!",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = "Masuk ke akun Anda",
            fontFamily = PoppinsFont,
            fontSize = 15.sp,
            color = Color(0xFF666666)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontFamily = PoppinsFont) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFCB507),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = PoppinsFont, fontSize = 15.sp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Kata Sandi", fontFamily = PoppinsFont) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFCB507),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = PoppinsFont, fontSize = 15.sp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe, 
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFCB507),
                        uncheckedColor = Color(0xFF666666)
                    )
                )
                Text("Ingat saya", fontFamily = PoppinsFont, fontSize = 14.sp, color = Color(0xFF666666))
            }
            Text(
                "Lupa Kata Sandi?", 
                fontFamily = PoppinsFont,
                fontSize = 14.sp, 
                color = Color(0xFFFCB507),
                modifier = Modifier.clickable { /* TODO: Forgot password */ }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                if (email.trim().isBlank()) {
                    errorMessage = "Email tidak boleh kosong"
                    return@Button
                }
                if (password.isBlank()) {
                    errorMessage = "Kata sandi tidak boleh kosong"
                    return@Button
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                    errorMessage = "Format email tidak valid"
                    return@Button
                }
                
                isLoading = true
                errorMessage = ""
                
                firebaseAuth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener { result ->
                        val user = result.user
                        user?.let { currentUser ->
                            // Save "Remember Me" preference
                            val sharedPref = context.getSharedPreferences("foca_prefs", android.content.Context.MODE_PRIVATE)
                            sharedPref.edit().putBoolean("remember_me", rememberMe).apply()
                            
                            // Log login
                            val loginLog = hashMapOf(
                                "userId" to currentUser.uid,
                                "email" to email.trim(),
                                "timestamp" to com.google.firebase.Timestamp.now(),
                                "method" to "email"
                            )
                            firestore.collection("login_history").add(loginLog)
                            
                            isLoading = false
                            Toast.makeText(context, "Selamat datang kembali!", Toast.LENGTH_SHORT).show()
                            
                            // Navigate to Home using BottomNavItem.Home.route
                            navController.navigate("home") {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        errorMessage = when {
                            e.localizedMessage?.contains("no user record") == true || 
                            e.localizedMessage?.contains("user not found") == true -> 
                                "Email tidak terdaftar. Silakan daftar terlebih dahulu."
                            e.localizedMessage?.contains("wrong password") == true || 
                            e.localizedMessage?.contains("invalid credential") == true -> 
                                "Kata sandi salah. Silakan coba lagi."
                            e.localizedMessage?.contains("too many requests") == true -> 
                                "Terlalu banyak percobaan. Coba lagi nanti."
                            e.localizedMessage?.contains("badly formatted") == true -> 
                                "Format email tidak valid."
                            else -> "Login gagal: ${e.localizedMessage}"
                        }
                    }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Masuk",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
        
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                errorMessage,
                fontFamily = PoppinsFont,
                color = Color(0xFFE53E3E),
                fontSize = 14.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE), thickness = 1.dp)
            Text(
                "  Atau masuk dengan  ",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color(0xFF999999)
            )
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE), thickness = 1.dp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Google Sign-In Button
        OutlinedButton(
            onClick = { onGoogleSignIn?.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Masuk dengan Google",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                fontSize = 15.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row {
            Text(
                "Belum punya akun? ",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            Text(
                text = "Daftar",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFCB507),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SIGNUP) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun IconLogin(drawableId: Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(48.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
