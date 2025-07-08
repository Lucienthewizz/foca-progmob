package com.example.foca.ui.screen

// Import Statements
import androidx.compose.foundation.*
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
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.interaction.MutableInteractionSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.net.URLEncoder
import androidx.compose.material3.DatePickerDialog
import kotlinx.coroutines.launch
import java.text.NumberFormat

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
    val gradientBrush = Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFFF8E1)))
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(gradientBrush)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ringkasan Pesanan", fontWeight = FontWeight.Bold, fontFamily = PoppinsFont, fontSize = 20.sp)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF0C4))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Receipt, null, tint = Color(0xFFFCB507), modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.ShoppingBag, null, tint = Color(0xFF757575), modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Jumlah Item", fontFamily = PoppinsFont, fontSize = 14.sp, color = Color(0xFF757575))
                    }
                    Text("$itemCount item", fontFamily = PoppinsFont, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF0C4))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Payments, null, tint = Color(0xFFFCB507), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Total Harga", fontFamily = PoppinsFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Rp $total", fontWeight = FontWeight.Bold, color = Color(0xFFFCB507), fontSize = 22.sp)
                }
            }
        }
    }
}

// === ClearCartDialog & CheckoutDialog ===
@Composable
fun ClearCartDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kosongkan Keranjang", fontFamily = PoppinsFont, fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.DeleteSweep, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Apakah Anda yakin ingin mengosongkan keranjang?", fontFamily = PoppinsFont, textAlign = TextAlign.Center)
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Kosongkan", fontFamily = PoppinsFont)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal", fontFamily = PoppinsFont)
            }
        }
    )
}

@Composable
fun CheckoutDialog(
    address: String,
    note: String,
    date: String,
    onAddressChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Checkout Pesanan", fontFamily = PoppinsFont, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = address, onValueChange = onAddressChange, label = { Text("Alamat", fontFamily = PoppinsFont) })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = note, onValueChange = onNoteChange, label = { Text("Catatan", fontFamily = PoppinsFont) })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = date, onValueChange = onDateChange, label = { Text("Tanggal (yyyy-MM-dd)", fontFamily = PoppinsFont) })
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Konfirmasi", fontFamily = PoppinsFont)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal", fontFamily = PoppinsFont)
            }
        }
    )
}

// === CartScreen ===
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val viewModel: CartViewModel = viewModel()
    val cateringViewModel: CateringViewModel = viewModel()
    val cartItems by viewModel.cartItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val operationStatus by viewModel.operationStatus.collectAsState()
    val allMenus = cateringViewModel.allItems.collectAsState(initial = emptyList()).value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var total = 0.0
    val cartDetails = cartItems.mapNotNull {
        val menu = allMenus.find { menu -> menu.id == it.cateringId }
        if (menu != null) {
            total += menu.price * it.quantity
            Triple(menu, it.quantity, menu.price * it.quantity)
        } else null
    }

    var showDialog by remember { mutableStateOf(false) }
    var showClearCartDialog by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

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
                title = { Text("Keranjang", fontSize = 22.sp, fontFamily = PoppinsFont) },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = { showClearCartDialog = true }) {
                            Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                cartItems.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Keranjang kosong", fontFamily = PoppinsFont)
                }
                else -> Column(Modifier.padding(16.dp)) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartDetails) { (menu, qty, subtotal) ->
                            CartItemCard(
                                menu = menu,
                                quantity = qty,
                                subtotal = subtotal,
                                onIncreaseQuantity = { userId?.let { viewModel.updateCartItemQuantity(it, menu.id, qty + 1) } },
                                onDecreaseQuantity = { userId?.let { viewModel.updateCartItemQuantity(it, menu.id, qty - 1) } },
                                onRemove = { userId?.let { viewModel.removeCartItem(it, menu.id) } }
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OrderSummaryCard(total = total, itemCount = cartItems.sumOf { it.quantity })
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("payment/$total")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Checkout", fontFamily = PoppinsFont)
                    }
                }
            }

            if (showClearCartDialog) {
                ClearCartDialog({
                    userId?.let { viewModel.clearCart(it) }
                    showClearCartDialog = false
                }, {
                    showClearCartDialog = false
                })
            }
        }
    }
}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(amount).replace(",00", "").replace("Rp", "Rp ")
}
