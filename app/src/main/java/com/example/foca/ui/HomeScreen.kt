package com.example.foca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.ui.theme.FocaTheme
import com.example.foca.ui.theme.PoppinsFont

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F0))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp, bottom = 24.dp)
    ) {
        // User greeting section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            // Profile image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFC107))
            ) {
                Text(
                    text = "🙂",
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = "Hello, Guest!",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Log in to access more feature",
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Main Title
        Text(
            text = "Ayo Temui",
            fontFamily = PoppinsFont,
            color = Color(0xFFFFC107),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Catering Favorit",
            fontFamily = PoppinsFont,
            color = Color(0xFFFFC107),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Anda!",
            fontFamily = PoppinsFont,
            color = Color(0xFFFFC107),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Jenis Katering
        Text(
            text = "Jenis Katering",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Catering type cards
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            KateringCard(
                type = "outdoor",
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .padding(end = 4.dp)
            )
            KateringCard(
                type = "outdoor", 
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .padding(start = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rekomendasi Menu
        Text(
            text = "Rekomendasi Menu",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Menu items grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 8.dp),
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            val menuItems = List(4) { "tumpeng" }
            items(menuItems) { menuType ->
                MenuCard(
                    type = menuType,
                    modifier = Modifier
                        .height(110.dp)
                        .padding(4.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Katering Populer
        Text(
            text = "Katering Populer",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Popular catering
        KateringCard(
            type = "populer",
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        )
    }
}

@Composable
fun KateringCard(type: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        when (type) {
            "outdoor" -> {
                Column {
                    // Image part (top 70%)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .background(Color(0xFF5C5C5C))
                    ) {
                        Text(
                            text = "Outdoor Catering",
                            fontFamily = PoppinsFont,
                            color = Color.White
                        )
                    }
                    // Yellow part (bottom 30%)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .background(Color(0xFFFFD54F))
                    )
                }
            }
            else -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF5C5C5C))
                ) {
                    Text(
                        text = "Katering Populer",
                        fontFamily = PoppinsFont,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(type: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4CAF50))
        ) {
            Text(
                text = "Tumpeng",
                fontFamily = PoppinsFont,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FocaTheme {
        HomeScreen()
    }
} 