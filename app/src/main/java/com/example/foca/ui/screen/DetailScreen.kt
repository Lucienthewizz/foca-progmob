package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.foca.data.model.CateringItem
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.foca.ui.theme.PoppinsFont
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foca.data.viewmodel.CateringViewModel
import com.example.foca.data.model.UserProfile
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.draw.clip
import com.example.foca.data.model.Comment
import java.util.Date
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf

@Composable
fun DetailScreen(item: CateringItem, onAddToCart: ((CateringItem) -> Unit)? = null, owner: UserProfile? = null) {
    val viewModel: CateringViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    val userPhotoUrl = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString() ?: ""
    var quantity = remember { mutableStateOf(1) }
    var commentText by remember { mutableStateOf("") }
    var commentRating by remember { mutableStateOf(5) }
    val comments by viewModel.comments.collectAsState()
    val isLoggedIn = userId != null
    LaunchedEffect(item.id) {
        viewModel.loadComments(item.id)
    }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFDFBF7))) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Gambar menu
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(top = 18.dp, start = 18.dp, end = 18.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.imageUrl),
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                // Judul & info
                Text(
                    text = item.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF222222),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.category ?: "",
                        fontSize = 14.sp,
                        color = Color(0xFFFCB507),
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "⭐ ${item.rating ?: 0.0}",
                        fontSize = 15.sp,
                        color = Color(0xFFFFA000),
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = formatRupiah(item.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCB507),
                        fontFamily = PoppinsFont
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                // Deskripsi
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = item.description,
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                // Info Catering Owner
                if (owner != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp, start = 18.dp, end = 18.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(owner.photoUrl),
                            contentDescription = "Owner Photo",
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(owner.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(owner.email, fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
                // Selector jumlah
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 18.dp, top = 8.dp)
                ) {
                    IconButton(onClick = { if (quantity.value > 1) quantity.value-- }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Kurangi")
                    }
                    Text(
                        text = quantity.value.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(36.dp),
                        color = Color.Black
                    )
                    IconButton(onClick = { quantity.value++ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Tambah")
                    }
                }
                Button(
                    onClick = {
                        if (userId != null) {
                            repeat(quantity.value) {
                                viewModel.addToCart(userId, item.id ?: "", onSuccess = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Berhasil ditambahkan ke keranjang!")
                                    }
                                })
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Silakan login terlebih dahulu.")
                            }
                        }
                        onAddToCart?.invoke(item)
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 18.dp)
                ) {
                    Text(text = "Tambah ke Keranjang", fontSize = 19.sp, fontFamily = PoppinsFont, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Komentar & Rating
                Text("Komentar & Rating", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp, start = 18.dp))
                // Form komentar
                if (isLoggedIn) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 4.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Tulis Komentar Anda", fontWeight = FontWeight.Medium, fontFamily = PoppinsFont, fontSize = 15.sp)
                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text("Tulis komentar...", fontFamily = PoppinsFont) },
                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                singleLine = false,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Rating:", fontFamily = PoppinsFont, fontSize = 14.sp)
                                Spacer(Modifier.width(8.dp))
                                for (i in 1..5) {
                                    IconButton(onClick = { commentRating = i }) {
                                        Icon(
                                            imageVector = if (i <= commentRating) Icons.Filled.Star else Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = if (i <= commentRating) Color(0xFFFFA000) else Color(0xFFE0E0E0),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.weight(1f))
                                Button(
                                    onClick = {
                                        if (commentText.isNotBlank()) {
                                            val comment = Comment(
                                                itemId = item.id,
                                                userId = userId ?: "",
                                                userName = userName,
                                                userPhotoUrl = userPhotoUrl,
                                                text = commentText,
                                                rating = commentRating,
                                                timestamp = Date().time
                                            )
                                            viewModel.addComment(comment)
                                            commentText = ""
                                            commentRating = 5
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Komentar berhasil dikirim!")
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    enabled = commentText.isNotBlank()
                                ) {
                                    Text("Kirim", fontFamily = PoppinsFont)
                                }
                            }
                        }
                    }
                }
                // Komentar quote style
                Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
                    if (comments.isEmpty()) {
                        Text("Belum ada komentar.", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                    } else {
                        comments.sortedByDescending { it.timestamp }.forEach { comment ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                                elevation = CardDefaults.cardElevation(0.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                            ) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
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
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
} 