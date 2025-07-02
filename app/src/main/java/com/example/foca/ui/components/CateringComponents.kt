package com.example.foca.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.data.model.CateringItem

/**
 * Komponen-komponen yang digunakan bersama oleh CateringEventScreen dan CateringDailyScreen
 */

@Composable
fun FirebaseRecommendationCard(
    item: CateringItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAddToCart: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .width(190.dp)
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Gambar di belakang (full size tanpa padding dan mentok ke pojok)
            GlideImageWithLoading(
                imageUrl = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay gradient di bagian bawah untuk memastikan teks terbaca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            )
                        )
                    )
            )
            
            // Badge kategori
            Text(
                text = item.category.replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier
                    .background(Color(0xFFFCB507), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 8.dp)
            )
            
            // Informasi di bagian bawah (hanya title, harga, dan perusahaan catering)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rp${item.price.toInt()}",
                        color = Color(0xFFFCB507),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    
                    // Perusahaan catering
                    Text(
                        text = "Foca Catering",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
            
            // Tombol + (add to cart) di pojok kanan bawah (ukuran lebih kecil)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    onClick = { onAddToCart?.invoke() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add to cart",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp).padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CateringTestimonialCard(
    userName: String,
    userPhotoUrl: String?,
    testimonial: String,
    rating: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (userPhotoUrl != null && userPhotoUrl.isNotEmpty()) {
                GlideImageWithLoading(
                    imageUrl = userPhotoUrl,
                    contentDescription = userName,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = com.example.foca.R.drawable.ic_john),
                    contentDescription = userName,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(testimonial, fontSize = 12.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(rating.toInt()) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    }
                    if (rating - rating.toInt() > 0.1f) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0x33FFC107), modifier = Modifier.size(14.dp))
                    }
                    Text("$rating", color = Color(0xFFFFC107), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(imageRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE082)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Rekomendasi",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Gambar di belakang (full size dan mentok ke pojok)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay gradient di bagian bawah untuk memastikan teks terbaca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            )
                        )
                    )
            )
            
            // Judul di bagian bawah
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }
}