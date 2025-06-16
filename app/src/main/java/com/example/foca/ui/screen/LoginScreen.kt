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

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text("Welcome Back!", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Create an account or log in to explore our app", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(Routes.HOME) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Log in", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Or login with", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(R.drawable.google, R.drawable.facebook, R.drawable.apple).forEach {
                IconLogin(it)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Don’t have an account? ", fontSize = 12.sp)
            Text(
                text = "Sign up",
                fontSize = 12.sp,
                color = Color(0xFFFCB507),
                modifier = Modifier.clickable {
                    // navigasi ke halaman register (jika sudah ada)
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
