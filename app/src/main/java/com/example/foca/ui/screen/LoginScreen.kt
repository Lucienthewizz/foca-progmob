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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.nav.Routes
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
            .background(Color(0xFFFDF8F3))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        // Logo/Brand
        Image(
            painter = painterResource(id = R.drawable.foco_transparant),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Welcome Back!", fontSize = 24.sp, color = Color.Black)
        Text("Log in to your account", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Text("Remember me", fontSize = 12.sp)
            }
            Text("Forgot Password?", fontSize = 12.sp, color = Color(0xFFFCB507))
        }
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val user = result.user
                        // Log ke Firestore
                        val loginLog = hashMapOf(
                            "userId" to (user?.uid ?: ""),
                            "email" to (user?.email ?: email),
                            "timestamp" to com.google.firebase.Timestamp.now(),
                            "method" to "email"
                        )
                        firestore.collection("login_history").add(loginLog)
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    .addOnFailureListener { e ->
                        errorMessage = e.localizedMessage ?: "Login gagal."
                    }
                    .addOnCompleteListener { isLoading = false }
            },
            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Log in", color = Color.White, fontSize = 16.sp)
        }
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text("  Or login with  ", fontSize = 12.sp, color = Color.Gray)
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(14.dp))
        // Google Sign-In Button
        Button(
            onClick = { onGoogleSignIn?.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(10.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign in with Google", color = Color.Black, fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Text("Don't have an account? ", fontSize = 12.sp)
            Text(
                text = "Sign up",
                fontSize = 12.sp,
                color = Color(0xFFFCB507),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SIGNUP)
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
