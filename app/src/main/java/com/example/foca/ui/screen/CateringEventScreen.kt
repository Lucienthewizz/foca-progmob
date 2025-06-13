package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R

@Composable
fun CateringEventScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6E9)) // cream background
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Catering Event",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (searchQuery.isEmpty()) {
                Text(
                    text = "Cari menu, paket, atau vendor...",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row 1: Harian & Bulanan
        Row(modifier = Modifier.fillMaxWidth()) {
            EventCategoryCard(
                title = "Harian",
                imageRes = R.drawable.ic_sandwich,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .height(160.dp)
            )
            EventCategoryCard(
                title = "Bulanan",
                imageRes = R.drawable.ic_sandwich,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
                    .height(160.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Mingguan
        Row(modifier = Modifier.fillMaxWidth()) {
            EventCategoryCard(
                title = "Mingguan",
                imageRes = R.drawable.ic_sandwich,
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Rekomendasi Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Rekomendasi Menu", fontSize = 18.sp, color = Color.Black)
            Text("more →", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Rekomendasi cards
        Row(modifier = Modifier.fillMaxWidth()) {
            RecommendationCard(
                imageRes = R.drawable.ic_restaurant,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .height(140.dp)
            )
            RecommendationCard(
                imageRes = R.drawable.ic_restaurant,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
                    .height(140.dp)
            )
        }
    }
}

@Composable
fun EventCategoryCard(title: String, imageRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(64.dp)
            )
            Text(text = title, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
fun RecommendationCard(imageRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE082)), // kuning seperti di Figma
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Rekomendasi",
            modifier = Modifier.fillMaxSize()
        )
    }
}
