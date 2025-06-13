package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
fun CateringDailyScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF6E9)) // cream background
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = "Catering Daily",
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
                Text(text = "Cari menu, paket, atau vendor...", color = Color.Gray, fontSize = 14.sp)
            }
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row 1: Appetizer & Main Course
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                title = "Appetizer",
                imageRes = R.drawable.appetizer,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .height(160.dp)
            )
            CategoryCard(
                title = "Main Course",
                imageRes = R.drawable.ic_restaurant,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
                    .height(160.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Dessert only (left aligned)
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                title = "Dessert",
                imageRes = R.drawable.dessert,
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
            )
            Spacer(modifier = Modifier.weight(1f)) // kosongkan kanan
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { },
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
