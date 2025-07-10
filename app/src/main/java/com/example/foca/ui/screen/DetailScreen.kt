package com.example.foca.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Hero Image Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageUrl),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Title and Basic Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = item.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Category and Rating Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = item.category ?: "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666),
                            fontFamily = PoppinsFont,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${item.rating ?: 0.0}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF666666),
                                fontFamily = PoppinsFont
                            )
                        }
                    }
                    
                    // Price
                    Text(
                        text = formatRupiah(item.price),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCB507),
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            
            // Description Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Deskripsi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = Color(0xFF666666),
                    fontFamily = PoppinsFont,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
            
            // Owner Info Section
            if (owner != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Informasi Catering",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(owner.photoUrl),
                            contentDescription = "Owner Photo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = owner.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                fontFamily = PoppinsFont,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = owner.email,
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                fontFamily = PoppinsFont
                            )
                        }
                    }
                }
            }
            
            // Quantity and Add to Cart Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Jumlah Pesanan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity.value > 1) quantity.value-- },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (quantity.value > 1) Color(0xFFFCB507) else Color(0xFFE0E0E0),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.Remove,
                            contentDescription = "Kurangi",
                            tint = if (quantity.value > 1) Color.White else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Text(
                        text = quantity.value.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = Color(0xFF1A1A1A)
                    )
                    
                    IconButton(
                        onClick = { quantity.value++ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFCB507), CircleShape)
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Tambah",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                // Add to Cart Button
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
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCB507)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Tambah ke Keranjang",
                        fontSize = 16.sp,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Comments Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Ulasan & Rating",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Comment Form
                if (isLoggedIn) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { 
                                Text(
                                    "Bagikan pengalaman Anda...",
                                    fontFamily = PoppinsFont,
                                    color = Color(0xFF999999)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            singleLine = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.White,
                                focusedBorderColor = Color(0xFFFCB507),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Rating and Send Button Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Rating:",
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            
                            // Stars Rating
                            Row(
                                modifier = Modifier.weight(1f)
                            ) {
                                for (i in 1..5) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = if (i <= commentRating) Color(0xFFFFA000) else Color(0xFFE0E0E0),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { commentRating = i }
                                            .padding(end = 2.dp)
                                    )
                                }
                            }
                            
                            // Send Button
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
                                            snackbarHostState.showSnackbar("Ulasan berhasil dikirim!")
                                        }
                                    }
                                },
                                enabled = commentText.isNotBlank(),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFCB507),
                                    disabledContainerColor = Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "Kirim",
                                    fontFamily = PoppinsFont,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Silakan login untuk memberikan ulasan",
                            fontFamily = PoppinsFont,
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Comments List
                if (comments.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum ada ulasan untuk item ini",
                            color = Color(0xFF999999),
                            fontSize = 14.sp,
                            fontFamily = PoppinsFont
                        )
                    }
                } else {
                    Column {
                        comments.sortedByDescending { it.timestamp }.forEach { comment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // User Avatar
                                if (comment.userPhotoUrl.isNotBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(comment.userPhotoUrl),
                                        contentDescription = "User Photo",
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                
                                // Comment Content
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // User name and rating
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Text(
                                            text = comment.userName,
                                            fontFamily = PoppinsFont,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1A1A1A),
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        
                                        // Rating stars
                                        Row {
                                            repeat(comment.rating) {
                                                Icon(
                                                    Icons.Filled.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFA000),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                            repeat(5 - comment.rating) {
                                                Icon(
                                                    Icons.Filled.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFE0E0E0),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                    
                                    // Comment text
                                    Text(
                                        text = comment.text,
                                        fontFamily = PoppinsFont,
                                        fontSize = 14.sp,
                                        color = Color(0xFF333333),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                            
                            // Divider
                            if (comment != comments.sortedByDescending { it.timestamp }.last()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFFE0E0E0))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // Snackbar host positioned at bottom
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
} 