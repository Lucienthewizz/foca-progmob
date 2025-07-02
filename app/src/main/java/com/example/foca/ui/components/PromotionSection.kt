package com.example.foca.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.R
import com.example.foca.ui.theme.PoppinsFont

/**
 * Data class untuk menyimpan informasi promosi
 */
data class PromotionData(
    val id: String,
    val title: String,
    val description: String,
    val imageRes: Int
)

/**
 * Komponen untuk menampilkan bagian promosi di halaman utama
 * 
 * @param promotions Daftar promosi yang akan ditampilkan
 * @param onPromotionClick Callback saat promosi diklik
 * @param modifier Modifier untuk menyesuaikan tampilan
 */
@Composable
fun PromotionSection(
    promotions: List<PromotionData>,
    onPromotionClick: (PromotionData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Text(
            text = "Promo Spesial Untukmu",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF222222)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Promotions
        if (promotions.isEmpty()) {
            Text(
                text = "Tidak ada promo saat ini",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color.Gray
            )
        } else {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(promotions) { promo ->
                    PromotionCard(
                        title = promo.title,
                        description = promo.description,
                        imageRes = promo.imageRes,
                        onClick = { onPromotionClick(promo) },
                        modifier = Modifier
                            .width(280.dp)
                            .padding(end = 16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Contoh penggunaan PromotionSection
 */
@Composable
fun SamplePromotions() {
    val samplePromotions = listOf(
        PromotionData(
            id = "promo1",
            title = "Diskon 20% Paket Harian",
            description = "Pesan paket harian untuk 1 minggu dan dapatkan diskon 20%",
            imageRes = R.drawable.ic_restaurant
        ),
        PromotionData(
            id = "promo2",
            title = "Gratis Pengiriman",
            description = "Nikmati gratis biaya pengiriman untuk pemesanan di atas Rp200.000",
            imageRes = R.drawable.ic_restaurant
        ),
        PromotionData(
            id = "promo3",
            title = "Paket Hemat Keluarga",
            description = "Pesan paket keluarga dan hemat hingga 30% dari harga normal",
            imageRes = R.drawable.ic_restaurant
        )
    )
    
    PromotionSection(
        promotions = samplePromotions,
        onPromotionClick = { /* Handle click */ }
    )
}