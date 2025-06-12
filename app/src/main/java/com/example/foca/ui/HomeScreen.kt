package com.example.foca.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.R
import com.example.foca.ui.theme.FocaTheme
import com.example.foca.ui.theme.PoppinsFont
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeContent(navController)
        }
        composable("catering_daily") {
            CateringDailyScreen()
        }
    }
}

@Composable
fun HomeContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F0))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 70.dp, bottom = 24.dp)
    ) {
        // Header: Profile & Greeting
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_john),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Hello, John!",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Siap memilih katering hari ini?",
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        // Search Bar
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cari menu, paket, atau vendor...",
                    color = Color(0xFFBDBDBD),
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp
                )
            }
        }
        // Kategori: Daily & Event
        Spacer(modifier = Modifier.height(45.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                title = "Daily",
                imageRes = R.drawable.ic_sandwich,
                modifier = Modifier.weight(1f).padding(end = 6.dp).height(199.dp),
                onClick = { navController.navigate("catering_daily") }
            )
            CategoryCard(
                title = "Event",
                imageRes = R.drawable.ic_stand,
                modifier = Modifier.weight(1f).padding(start = 6.dp).height(199.dp),
                onClick = {}
            )
        }
        // Rekomendasi Menu
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rekomendasi Menu",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "more →",
                fontFamily = PoppinsFont,
                fontSize = 12.sp,
                color = Color(0xFFBDBDBD)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            RecommendationCard(modifier = Modifier.weight(1f).padding(end = 6.dp))
            RecommendationCard(modifier = Modifier.weight(1f).padding(start = 6.dp))
        }
        // Paket Katering Favorit Minggu Ini
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Paket Katering Favorit Minggu Ini 🍒",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            RecommendationCard(modifier = Modifier.weight(1f).padding(end = 6.dp))
            RecommendationCard(modifier = Modifier.weight(1f).padding(start = 6.dp))
        }
    }
}

@Composable
fun CategoryCard(title: String, imageRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.height(199.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDF6E9))
        ) {
            Text(
                text = title,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomEnd)
                    .padding(top = 50.dp, start = 40.dp)
            )
        }
    }
}

@Composable
fun RecommendationCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_restaurant),
                    contentDescription = "Menu Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .background(Color(0xFFFFD54F))
            ) {}
        }
    }
}

@Composable
fun CateringDailyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF2))
            .padding(16.dp)
    ) {
        Text(
            text = "Catering Daily",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        // Tambahkan konten lain sesuai kebutuhan
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FocaTheme {
        HomeScreen()
    }
} 