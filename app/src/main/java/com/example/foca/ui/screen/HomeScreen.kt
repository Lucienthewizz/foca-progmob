package com.example.foca.ui.screen

// Using TestimonialCard from CardComponents.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foca.R
import com.example.foca.data.viewmodel.CateringViewModel
import com.example.foca.ui.components.CategoryCard
import com.example.foca.ui.components.FoodCard
import com.example.foca.ui.components.PromotionData
import com.example.foca.ui.components.PromotionSection
import com.example.foca.ui.components.SearchFilterComponent
import com.example.foca.ui.components.CateringTestimonialCard
import com.example.foca.ui.nav.Routes
import com.example.foca.ui.theme.PoppinsFont
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import com.example.foca.data.model.Comment


@Composable
fun HomeScreen(navController: NavController, userName: String? = null, userPhotoUrl: String? = null, paymentSuccess: Boolean = false) {
    val viewModel: CateringViewModel = viewModel()
    val recommendedMenus = viewModel.recommendedMenus.collectAsState(initial = emptyList()).value
    val favoriteMenus = viewModel.favoriteMenus.collectAsState(initial = emptyList()).value
    val isLoading = viewModel.isLoading.collectAsState().value
    val allItems = viewModel.allItems.collectAsState(initial = emptyList()).value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val (selectedCategory, setSelectedCategory) = remember { mutableStateOf("Semua") }
    val (selectedRating, setSelectedRating) = remember { mutableDoubleStateOf(0.0) }
    val (sortOption, setSortOption) = remember { mutableStateOf("Default") }
    val categoryOptions = listOf("Semua") + allItems.map { it.category }.distinct().filter { it.isNotBlank() }
    val sortOptions = listOf("Default", "Harga Termurah", "Rating Tertinggi")
    val ratingOptions = listOf(0.0, 3.0, 4.0, 4.5, 5.0)
    var showSuccessDialog by remember { mutableStateOf(paymentSuccess) }
    val latestComments by viewModel.latestComments.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAllCateringItems()
        viewModel.loadRecommendedMenus()
        viewModel.loadFavoriteMenus()
        viewModel.loadLatestComments(10)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFBF7))
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
        ) {
            // Header: Profile & Greeting
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                if (!userPhotoUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(userPhotoUrl),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(RoundedCornerShape(17.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_john),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(RoundedCornerShape(17.dp))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Hello, ${userName ?: "User"}!",
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
            // Search Component
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchFilterComponent(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { setSearchQuery(it) },
                    selectedCategory = "",
                    onCategorySelected = {},
                    selectedRating = 0.0,
                    onRatingSelected = {},
                    sortOption = "",
                    onSortOptionSelected = {},
                    categoryOptions = emptyList(),
                    ratingOptions = emptyList(),
                    sortOptions = emptyList(),
                    onResetFilters = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (searchQuery.isNotBlank()) {
                // Hanya tampilkan hasil pencarian
                Text(
                    text = "Hasil Pencarian",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF222222)
                )
                Spacer(modifier = Modifier.height(12.dp))
                val filteredItems = allItems.filter { item ->
                    val query = searchQuery.trim().lowercase()
                    item.title.lowercase().contains(query) || item.description.lowercase().contains(query)
                }
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFCB507))
                    }
                } else if (filteredItems.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Tidak ada hasil",
                            modifier = Modifier.size(120.dp).padding(top = 24.dp)
                        )
                        Text("Tidak ada menu yang sesuai.", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp))
                    }
                } else {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(filteredItems) { item ->
                            FoodCard(
                                item = item,
                                modifier = Modifier.padding(end = 14.dp),
                                onClick = {
                                    navController.navigate("detail/${item.id}")
                                },
                                onAddToCart = {
                                    if (userId != null) {
                                        viewModel.addToCart(userId, item.id ?: "", onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Berhasil ditambahkan ke keranjang!")
                                            }
                                        })
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Silakan login terlebih dahulu.")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Kategori: Daily & Event
                Spacer(modifier = Modifier.height(24.dp))
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
                        fontSize = 18.sp,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFCB507))
                    }
                } else if (recommendedMenus.isEmpty()) {
                    Text("Tidak ada rekomendasi.", color = Color.Gray)
                } else {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(recommendedMenus) { item ->
                            FoodCard(
                                item = item,
                                modifier = Modifier.padding(end = 14.dp),
                                onClick = {
                                    navController.navigate("detail/${item.id}")
                                },
                                onAddToCart = {
                                    if (userId != null) {
                                        viewModel.addToCart(userId, item.id ?: "", onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Berhasil ditambahkan ke keranjang!")
                                            }
                                        })
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Silakan login terlebih dahulu.")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                // Bagian Promosi
                Spacer(modifier = Modifier.height(28.dp))
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
                    onPromotionClick = { promo ->
                        // Handle promotion click
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Promo ${promo.title} dipilih")
                        }
                    }
                )
                
                // Paket Katering Favorit Minggu Ini
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Paket Katering Favorit Minggu Ini 🍒",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF222222)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFCB507))
                    }
                } else if (favoriteMenus.isEmpty()) {
                    Text("Tidak ada paket favorit.", color = Color.Gray)
                } else {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(favoriteMenus) { item ->
                            FoodCard(
                                item = item,
                                modifier = Modifier.padding(end = 14.dp),
                                onClick = {
                                    navController.navigate("detail/${item.id}")
                                },
                                onAddToCart = {
                                    if (userId != null) {
                                        viewModel.addToCart(userId, item.id ?: "", onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Berhasil ditambahkan ke keranjang!")
                                            }
                                        })
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Silakan login terlebih dahulu.")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                // Filter & Sort UI sudah dipindahkan ke komponen terpisah
                // Semua Menu
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Semua Menu",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF222222)
                )
                Spacer(modifier = Modifier.height(8.dp))
                var filteredItems = allItems
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFCB507))
                    }
                } else if (filteredItems.isEmpty()) {
                    Text("Tidak ada menu.", color = Color.Gray)
                } else {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(filteredItems) { item ->
                            FoodCard(
                                item = item,
                                modifier = Modifier.padding(end = 14.dp),
                                onClick = {
                                    navController.navigate("detail/${item.id}")
                                },
                                onAddToCart = {
                                    if (userId != null) {
                                        viewModel.addToCart(userId, item.id ?: "", onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Berhasil ditambahkan ke keranjang!")
                                            }
                                        })
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Silakan login terlebih dahulu.")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
            // Section Testimoni
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Testimoni Pelanggan",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF222222)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (latestComments.isEmpty()) {
                Text("Belum ada testimoni.", color = Color.Gray, fontSize = 14.sp)
            } else {
                // Auto-slide carousel (jika tidak bisa, pakai LazyRow biasa)
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(latestComments) { comment ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                            elevation = CardDefaults.cardElevation(0.dp),
                            modifier = Modifier
                                .width(320.dp)
                                .padding(end = 14.dp)
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.FormatQuote, contentDescription = null, tint = Color(0xFFFCB507), modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(comment.text, fontFamily = PoppinsFont, fontSize = 15.sp, color = Color(0xFF222222))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        repeat(comment.rating) {
                                            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(16.dp))
                                        }
                                        if (comment.rating < 5) {
                                            repeat(5 - comment.rating) {
                                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFE0E0E0), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text("oleh ${comment.userName}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                if (comment.userPhotoUrl.isNotBlank()) {
                                    Spacer(Modifier.width(8.dp))
                                    Image(
                                        painter = rememberAsyncImagePainter(comment.userPhotoUrl),
                                        contentDescription = "User Photo",
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White, CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Pembayaran Berhasil!", fontWeight = FontWeight.Bold) },
                text = { Text("Terima kasih, pesanan Anda telah dicatat. Silakan cek riwayat di profil.") },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

