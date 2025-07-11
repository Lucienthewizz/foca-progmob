package com.example.foca.ui.screen

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
import androidx.compose.foundation.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Validation functions
    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun validateForm(): String? {
        return when {
            firstName.isBlank() -> "Nama depan tidak boleh kosong"
            lastName.isBlank() -> "Nama belakang tidak boleh kosong"
            email.isBlank() -> "Email tidak boleh kosong"
            !validateEmail(email) -> "Format email tidak valid"
            password.isBlank() -> "Kata sandi tidak boleh kosong"
            !validatePassword(password) -> "Kata sandi minimal 6 karakter"
            confirmPassword.isBlank() -> "Konfirmasi kata sandi tidak boleh kosong"
            password != confirmPassword -> "Kata sandi dan konfirmasi tidak sama"
            else -> null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Image(
            painter = painterResource(id = R.drawable.foco_transparant),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Mulai Sekarang",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF1A1A1A)
        )
        Text(
            "Buat akun baru Anda",
            fontFamily = PoppinsFont,
            fontSize = 15.sp,
            color = Color(0xFF666666)
        )
        
        Spacer(modifier = Modifier.height(28.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nama Depan", fontFamily = PoppinsFont) },
                modifier = Modifier
                    .weight(1f)
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
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nama Belakang", fontFamily = PoppinsFont) },
                modifier = Modifier
                    .weight(1f)
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
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Tanggal Lahir (opsional)", fontFamily = PoppinsFont) },
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
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Nomor Telepon (opsional)", fontFamily = PoppinsFont) },
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Kata Sandi", fontFamily = PoppinsFont) },
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
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                val validationError = validateForm()
                if (validationError != null) {
                    errorMessage = validationError
                    return@Button
                }
                
                isLoading = true
                errorMessage = ""
                
                firebaseAuth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener { result ->
                        val user = result.user
                        user?.let { currentUser ->
                            // Create user profile in Firestore
                            val userProfile = hashMapOf(
                                "userId" to currentUser.uid,
                                "firstName" to firstName.trim(),
                                "lastName" to lastName.trim(),
                                "name" to "${firstName.trim()} ${lastName.trim()}",
                                "email" to email.trim(),
                                "birthDate" to birthDate.trim(),
                                "phone" to phoneNumber.trim(),
                                "photoUrl" to "",
                                "role" to if (email.trim().lowercase() == "admin@foca.com") "admin" else "user",
                                "createdAt" to com.google.firebase.Timestamp.now()
                            )
                            
                            firestore.collection("users").document(currentUser.uid)
                                .set(userProfile)
                                .addOnSuccessListener {
                                    // Log registration
                                    val registerLog = hashMapOf(
                                        "userId" to currentUser.uid,
                                        "email" to email.trim(),
                                        "timestamp" to com.google.firebase.Timestamp.now(),
                                        "method" to "email"
                                    )
                                    firestore.collection("register_history").add(registerLog)
                                    
                                    isLoading = false
                                    Toast.makeText(context, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
                                    
                                    // Save "Remember Me" preference as true for new users
                                    val sharedPref = context.getSharedPreferences("foca_prefs", android.content.Context.MODE_PRIVATE)
                                    sharedPref.edit().putBoolean("remember_me", true).apply()
                                    
                                    // Navigate to Home using BottomNavItem.Home.route
                                    navController.navigate("home") {
                                        popUpTo(Routes.SIGNUP) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMessage = "Gagal membuat profil: ${e.localizedMessage}"
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        errorMessage = when {
                            e.localizedMessage?.contains("email address is already in use") == true -> 
                                "Email sudah terdaftar. Silakan gunakan email lain."
                            e.localizedMessage?.contains("weak password") == true -> 
                                "Kata sandi terlalu lemah. Gunakan minimal 6 karakter."
                            e.localizedMessage?.contains("badly formatted") == true -> 
                                "Format email tidak valid."
                            else -> "Daftar gagal: ${e.localizedMessage}"
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
                    "Ayo Mulai!",
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
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE), thickness = 1.dp)
            Text(
                "  Atau daftar dengan  ",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color(0xFF999999)
            )
            Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE), thickness = 1.dp)
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        OutlinedButton(
            onClick = { /* TODO: Google Auth logic */ },
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
                "Daftar dengan Google",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                fontSize = 15.sp
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Row {
            Text(
                "Sudah punya akun? ",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            Text(
                text = "Masuk",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFCB507),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
