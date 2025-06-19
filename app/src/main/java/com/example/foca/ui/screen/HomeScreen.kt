package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foca.R
import com.example.foca.ui.theme.PoppinsFont
import com.example.foca.ui.nav.Routes

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
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
                    .clip(RoundedCornerShape(17.dp))
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
                onClick = { navController.navigate(Routes.CATERING_DAILY) }
            )
            CategoryCard(
                title = "Event",
                imageRes = R.drawable.ic_stand,
                modifier = Modifier.weight(1f).padding(start = 6.dp).height(199.dp),
                onClick = { navController.navigate(Routes.CATERING_EVENT)}
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

