package com.example.foca.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.R
import com.example.foca.data.model.CateringItem
import com.example.foca.ui.theme.PoppinsFont

/**
 * Komponen card yang dinamis dan dapat digunakan kembali
 * Mengikuti prinsip UI/UX modern dengan desain yang bersih dan profesional
 */

/**
 * FoodCard - Komponen card untuk menampilkan item makanan
 * 
 * @param item Data item makanan yang akan ditampilkan
 * @param onClick Callback saat card diklik
 * @param onAddToCart Callback saat tombol tambah ke keranjang diklik
 * @param modifier Modifier untuk menyesuaikan tampilan
 * @param showAddButton Apakah tombol tambah ke keranjang ditampilkan
 */
@Composable
fun FoodCard(
    item: CateringItem,
    onClick: () -> Unit,
    onAddToCart: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    showAddButton: Boolean = true
) {
    Card(
        modifier = modifier
            .width(200.dp) // Lebar disesuaikan untuk proporsi yang lebih baik
            .height(260.dp) // Tinggi sedikit ditambah
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x1A000000))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Gambar di belakang (full size dan mentok ke pojok)
            GlideImageWithLoading(
                imageUrl = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay gradient di seluruh card untuk efek gelap
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x40000000), // Overlay hitam tipis di atas
                                Color(0x80000000), // Overlay hitam medium di tengah
                                Color(0xB0000000)  // Overlay hitam lebih pekat di bawah
                            )
                        )
                    )
            )
            
            // Category Badge
            if (item.category.isNotEmpty()) {
                Surface(
                    color = Color(0xFFFCB507),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = item.category.replaceFirstChar { it.uppercase() },
                        color = Color.White,
                        fontSize = 10.sp,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            // Rating Badge - Dipindahkan ke pojok kiri atas di sebelah category badge
            if (item.rating != null && item.rating > 0) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 10.dp, top = 45.dp) // Posisi di bawah category badge
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", item.rating),
                            color = Color(0xFF333333),
                            fontSize = 9.sp,
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Add to cart button di pojok kanan atas
            if (showAddButton && onAddToCart != null) {
                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(28.dp), // Sedikit diperbesar
                    onClick = { onAddToCart() }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            // Bottom content box with themed background
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(110.dp) // Ditambah tingginya untuk spacing yang lebih baik
            ) {
                // Gradient background for bottom box
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x00000000), // Transparent at top
                                    Color(0x80000000), // Medium opacity di tengah
                                    Color(0xE6000000)  // Almost solid black at bottom
                                ),
                                startY = 0f,
                                endY = 300f // Memperpanjang gradient untuk transisi yang lebih halus
                            )
                        )
                )
                
                // Content inside bottom box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp) // Padding lebih besar
                ) {
                    // Title dengan ukuran lebih besar
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 16.sp, // Ukuran font lebih besar
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = 0.2.sp // Sedikit letter spacing untuk keterbacaan
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp)) // Jarak lebih besar
                    
                    // Perusahaan catering
                    Text(
                        text = "Foca Catering",
                        color = Color.White.copy(alpha = 0.8f), // Opacity ditingkatkan sedikit
                        fontSize = 12.sp, // Ukuran font sedikit lebih besar
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Medium, // Medium weight untuk keterbacaan
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp)) // Jarak lebih besar sebelum harga
                    
                    // Price
                    Text(
                        text = "Rp${item.price.toInt()}",
                        color = Color(0xFFFCB507),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp, // Ukuran font lebih besar
                        fontFamily = PoppinsFont,
                        letterSpacing = 0.3.sp // Sedikit letter spacing untuk keterbacaan
                    )
                }
            }
        }
    }
}

/**
 * CategoryCard - Komponen card untuk menampilkan kategori
 * 
 * @param title Judul kategori
 * @param imageRes Resource ID gambar kategori
 * @param onClick Callback saat card diklik
 * @param modifier Modifier untuk menyesuaikan tampilan
 * @param backgroundColor Warna latar belakang card
 */
@Composable
fun CategoryCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFDF6E9)
) {
    Card(
        modifier = modifier
            .height(199.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x1A000000))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
            
            // Gambar di belakang (ukuran lebih kecil tapi tetap mentok ke pojok kanan bawah)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Mengecilkan gambar menjadi 80% dari lebar
                    .fillMaxHeight(0.8f) // Mengecilkan gambar menjadi 80% dari tinggi
                    .align(Alignment.BottomEnd),
                contentScale = ContentScale.Crop
            )
            
            // Overlay gradient hanya di bagian bawah (bagian atas bening)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f) // Hanya 60% dari tinggi card dari bawah
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, // Bagian atas gradient transparan
                                Color(0x99000000), // Bagian tengah semi-transparan
                                Color(0xD0000000)  // Bagian bawah lebih pekat
                            )
                        )
                    )
            )
            
            // Title di bagian bawah
            Column(modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                
                // Decorative line
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFCB507), Color(0xFFFFD54F))
                            ),
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
            }
        }
    }
}

/**
 * TestimonialCard - Komponen card untuk menampilkan testimoni pelanggan
 * 
 * @param userName Nama pengguna
 * @param userPhotoUrl URL foto pengguna (opsional)
 * @param testimonial Teks testimoni
 * @param rating Rating (1-5)
 * @param modifier Modifier untuk menyesuaikan tampilan
 */
@Composable
fun TestimonialCard(
    userName: String,
    userPhotoUrl: String?,
    testimonial: String,
    rating: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0x1A000000)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            // User photo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (userPhotoUrl != null && userPhotoUrl.isNotEmpty()) {
                    GlideImageWithLoading(
                        imageUrl = userPhotoUrl,
                        contentDescription = userName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_john),
                        contentDescription = userName,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        tint = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(modifier = Modifier.weight(1f)) {
                // User name and rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )
                    
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            val starColor = if (index < rating.toInt()) {
                                Color(0xFFFFC107) // Full star
                            } else if (index < Math.ceil(rating.toDouble())) {
                                Color(0x80FFC107) // Half star
                            } else {
                                Color(0x40FFC107) // Empty star
                            }
                            
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = starColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", rating),
                            color = Color(0xFFFFC107),
                            fontSize = 12.sp,
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Testimonial text
                Text(
                    text = testimonial,
                    fontSize = 13.sp,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF757575),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * PromotionCard - Komponen card untuk menampilkan promosi
 * 
 * @param title Judul promosi
 * @param description Deskripsi promosi
 * @param imageRes Resource ID gambar promosi
 * @param onClick Callback saat card diklik
 * @param modifier Modifier untuk menyesuaikan tampilan
 */
@Composable
fun PromotionCard(
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0x1A000000))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Content
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF333333)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = description,
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        "Lihat Detail",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Image
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}