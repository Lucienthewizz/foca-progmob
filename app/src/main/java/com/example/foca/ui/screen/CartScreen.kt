package com.example.foca.ui.screen

// Import Statements
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foca.data.model.CateringItem
import com.example.foca.data.viewmodel.CartViewModel
import com.example.foca.data.viewmodel.CateringViewModel
import com.example.foca.ui.theme.PoppinsFont
import com.example.foca.utils.formatRupiah
import com.google.firebase.auth.FirebaseAuth

// === CartItemCard ===
@Composable
fun CartItemCard(
    menu: CateringItem,
    quantity: Int,
    subtotal: Double,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = menu.imageUrl),
                    contentDescription = menu.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = menu.title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF333333)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatRupiah(menu.price),
                        color = Color(0xFFFCB507),
                        fontFamily = PoppinsFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE))
                            .clickable { onDecreaseQuantity() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Remove, "Kurangi", tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$quantity",
                            fontFamily = PoppinsFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF333333)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFCB507))
                            .clickable { onIncreaseQuantity() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Add, "Tambah", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEEEE))
                            .border(1.dp, Color(0xFFFFCCCC), CircleShape)
                            .clickable { onRemove() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Delete, "Hapus", tint = Color.Red, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Text(
                text = formatRupiah(subtotal),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = PoppinsFont,
                fontSize = 15.sp
            )
        }
    }
}

// === OrderSummaryCard ===
@Composable
fun OrderSummaryCard(total: Double, itemCount: Int, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ringkasan Pesanan",
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF0C4))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Receipt,
                        contentDescription = null,
                        tint = Color(0xFFFCB507),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Items count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Item ($itemCount)",
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = formatRupiah(total),
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color(0xFF333333)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Delivery fee
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Biaya Kirim",
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = formatRupiah(5000.0),
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color(0xFF333333)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontFamily = PoppinsFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = formatRupiah(total + 5000),
                    fontFamily = PoppinsFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFCB507)
                )
            }
        }
    }
}

// === ClearCartDialog & CheckoutDialog ===
@Composable
fun ClearCartDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = { 
            Text(
                "Kosongkan Keranjang",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A1A1A)
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEEEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.DeleteSweep,
                        contentDescription = null,
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Apakah Anda yakin ingin mengosongkan keranjang? Tindakan ini tidak dapat dibatalkan.",
                    fontFamily = PoppinsFont,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Kosongkan",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Batal",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

// === CartScreen ===
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cateringViewModel: CateringViewModel = viewModel()) {
    val viewModel: CartViewModel = viewModel()
    val cartItems by viewModel.cartItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val operationStatus by viewModel.operationStatus.collectAsState()
    val allMenus = cateringViewModel.allItems.collectAsState(initial = emptyList()).value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var total = 0.0
    val cartDetails = cartItems.mapNotNull { cartItem ->
        val menu = allMenus.find { menu -> menu.id == cartItem.cateringId }
        if (menu != null) {
            total += menu.price * cartItem.quantity
            Triple(menu, cartItem.quantity, menu.price * cartItem.quantity)
        } else null
    }

    var showClearCartDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadCartItems(userId)
            cateringViewModel.loadAllCateringItems()
        }
    }

    LaunchedEffect(operationStatus) {
        operationStatus?.let {
            snackbarHostState.showSnackbar(
                when (it) {
                    is CartViewModel.OperationStatus.Success -> it.message
                    is CartViewModel.OperationStatus.Error -> it.message
                    else -> "Unknown status"
                }
            )
            viewModel.resetOperationStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Keranjang Saya",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = { showClearCartDialog = true }) {
                            Icon(
                                Icons.Outlined.DeleteSweep,
                                contentDescription = "Kosongkan keranjang",
                                tint = Color(0xFFE53E3E)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color(0xFFF8F9FA),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFCB507)
                        )
                    }
                }
                cartItems.isEmpty() -> {
                    EmptyCartState()
                }
                else -> {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cartDetails) { (menu, quantity, subtotal) ->
                                CartItemCard(
                                    menu = menu,
                                    quantity = quantity,
                                    subtotal = subtotal,
                                    onIncreaseQuantity = {
                                        userId?.let { viewModel.updateCartItemQuantity(it, menu.id ?: "", quantity + 1) }
                                    },
                                    onDecreaseQuantity = {
                                        if (quantity > 1) {
                                            userId?.let { viewModel.updateCartItemQuantity(it, menu.id ?: "", quantity - 1) }
                                        }
                                    },
                                    onRemove = {
                                        userId?.let { viewModel.removeCartItem(it, menu.id ?: "") }
                                    }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OrderSummaryCard(
                            total = total,
                            itemCount = cartItems.sumOf { it.quantity }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { 
                                // Navigate to PaymentScreen dengan total
                                navController.navigate("payment/${total + 5000}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFCB507)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Checkout • ${formatRupiah(total + 5000)}",
                                    fontSize = 16.sp,
                                    fontFamily = PoppinsFont,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            
            if (showClearCartDialog) {
                ClearCartDialog(
                    onConfirm = {
                        userId?.let { viewModel.clearCart(it) }
                        showClearCartDialog = false
                    },
                    onDismiss = { showClearCartDialog = false }
                )
            }
        }
    }
}

@Composable
fun EmptyCartState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFFCCCCCC),
                    modifier = Modifier.size(60.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Keranjang Anda Kosong",
                fontFamily = PoppinsFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Tambahkan beberapa makanan lezat ke keranjang Anda",
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )
        }
    }
}
