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
import androidx.compose.ui.draw.clip

@Composable
fun DetailScreen(item: CateringItem, onAddToCart: ((CateringItem) -> Unit)? = null, owner: UserProfile? = null) {
    val viewModel: CateringViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var quantity = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(1) }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFDFBF7))) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageUrl),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = item.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = item.category ?: "",
                    fontSize = 14.sp,
                    color = Color(0xFFFCB507),
                    fontFamily = PoppinsFont,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${item.rating ?: 0.0}",
                        fontSize = 15.sp,
                        color = Color(0xFFFFA000),
                        fontFamily = PoppinsFont,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = formatRupiah(item.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCB507),
                        fontFamily = PoppinsFont
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    fontFamily = PoppinsFont,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
                // Info Catering Owner
                if (owner != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(owner.photoUrl),
                            contentDescription = "Owner Photo",
                            modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(owner.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(owner.email, fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
                // Selector jumlah
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 18.dp)
                ) {
                    IconButton(onClick = { if (quantity.value > 1) quantity.value-- }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Kurangi")
                    }
                    Text(
                        text = quantity.value.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(32.dp),
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(text = "Tambah ke Keranjang", fontSize = 18.sp, fontFamily = PoppinsFont)
                }
                Spacer(modifier = Modifier.height(18.dp))
                // Komentar & Rating (dummy)
                Text("Komentar & Rating", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            }
            items(listOf(
                Pair("Makanan enak dan pengiriman cepat!", 5),
                Pair("Porsi banyak, recommended.", 4),
                Pair("Rasa oke, harga terjangkau.", 4)
            )) { (comment, rating) ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⭐".repeat(rating), color = Color(0xFFFFA000), fontSize = 14.sp, modifier = Modifier.width(50.dp))
                        Text(comment, fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
} 