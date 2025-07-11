package com.example.foca.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foca.R
import com.example.foca.data.model.Order
import com.example.foca.data.model.CateringItem
import com.example.foca.data.viewmodel.AdminViewModel
import com.example.foca.ui.theme.PoppinsFont
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foca.utils.formatRupiah

@Composable
fun AdminScreen(navController: NavController, userId: String? = null, onLogout: (() -> Unit)? = null) {
    val viewModel: AdminViewModel = viewModel()
    val ordersState = viewModel.orders.collectAsState()
    val menuItemsState = viewModel.menuItems.collectAsState()
    val context = LocalContext.current
    
    var selectedTab by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddMenuDialog by remember { mutableStateOf(false) }
    var showOrderDetailDialog by remember { mutableStateOf<Order?>(null) }
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadAllOrders()
        viewModel.loadAllMenuItems()
    }

    val orders = ordersState.value
    val menuItems = menuItemsState.value
    
    // Calculate stats
    val totalOrders = orders.size
    val totalMenuItems = menuItems.size
    val totalRevenue = orders.filter { it.status == "success" }.sumOf { it.total ?: 0.0 }
    
    // Monitor data loading state - improved logic
    LaunchedEffect(orders, menuItems) {
        // Set data as loaded once we have any data or after a reasonable delay
        isDataLoaded = true
    }
    
    // Auto-set data loaded after timeout to prevent infinite loading
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000) // 2 seconds timeout
        isDataLoaded = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFCB507), // Gold/Yellow
                        Color(0xFFFDE68A), // Soft yellow
                        Color(0xFFF8F9FA)  // Light background
                    ),
                    startY = 0f,
                    endY = 1200f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Admin Header dengan design yang lebih clean dan modern
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Column {
                    Text(
                        text = "Dashboard Admin",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFont,
                        color = Color.White
                    )
                    Text(
                        text = "Selamat datang kembali!",
                        fontSize = 16.sp,
                        fontFamily = PoppinsFont,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                IconButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            // Stats Cards - Enhanced with dynamic data
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    EnhancedAdminStatCard(
                        title = "Total Pesanan",
                        count = totalOrders.toString(),
                        icon = Icons.Filled.ShoppingCart,
                        color = Color(0xFFFFA726),
                        bgColor = Color(0xFFFFF3E0),
                        isLoading = !isDataLoaded
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    EnhancedAdminStatCard(
                        title = "Total Menu",
                        count = totalMenuItems.toString(),
                        icon = Icons.Filled.MenuBook,
                        color = Color(0xFF66BB6A),
                        bgColor = Color(0xFFE8F5E9),
                        isLoading = !isDataLoaded
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    EnhancedAdminStatCard(
                        title = "Total Pendapatan",
                        count = formatRupiah(totalRevenue),
                        icon = Icons.Filled.AttachMoney,
                        color = Color(0xFF42A5F5),
                        bgColor = Color(0xFFE3F2FD),
                        isLoading = !isDataLoaded
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Enhanced Tab Navigation with better spacingsss
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFFCB507),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color(0xFFFCB507),
                            height = 4.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = if (selectedTab == 0) Color(0xFFFCB507) else Color(0xFF999999),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Pesanan",
                                fontFamily = PoppinsFont,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 0) Color(0xFFFCB507) else Color(0xFF999999)
                            )
                        }
                    }
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = if (selectedTab == 1) Color(0xFFFCB507) else Color(0xFF999999),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Menu",
                                fontFamily = PoppinsFont,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 1) Color(0xFFFCB507) else Color(0xFF999999)
                            )
                        }
                    }
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = if (selectedTab == 2) Color(0xFFFCB507) else Color(0xFF999999),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Chat",
                                fontFamily = PoppinsFont,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 2) Color(0xFFFCB507) else Color(0xFF999999)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Tab Content with better padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> OrderManagementTab(
                        orders = orders,
                        onOrderClick = { showOrderDetailDialog = it },
                        onStatusChange = { orderId, newStatus ->
                            viewModel.updateOrderStatus(orderId, newStatus)
                        }
                    )
                    1 -> MenuManagementTab(
                        menuItems = menuItems,
                        onAddClick = { showAddMenuDialog = true },
                        onDeleteClick = { itemId ->
                            viewModel.deleteMenuItem(itemId)
                        }
                    )
                    2 -> ChatManagementTab(
                        navController = navController
                    )
                }
            }
        }
        
        // Enhanced Dialogs
        if (showLogoutDialog) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) { },
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .shadow(16.dp, RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icon
                            Card(
                                modifier = Modifier.size(60.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                                shape = CircleShape
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "👋",
                                        fontSize = 28.sp
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Konfirmasi Logout",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF1A1A1A),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Apakah Anda yakin ingin keluar dari Admin Panel?",
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { showLogoutDialog = false },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Batal",
                                        fontFamily = PoppinsFont,
                                        color = Color(0xFF666666),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                
                                Button(
                                    onClick = {
                                        showLogoutDialog = false
                                        onLogout?.invoke()
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Ya, Logout",
                                        fontFamily = PoppinsFont,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        showOrderDetailDialog?.let { order ->
            OrderDetailDialog(
                order = order,
                onDismiss = { showOrderDetailDialog = null },
                onStatusChange = { newStatus ->
                    viewModel.updateOrderStatus(order.orderId, newStatus)
                    showOrderDetailDialog = null
                }
            )
        }
        
        if (showAddMenuDialog) {
            AddMenuDialog(
                onDismiss = { showAddMenuDialog = false },
                onAdd = { title, description, price, category, imageUrl ->
                    viewModel.addMenuItem(title, description, price, category, imageUrl)
                    showAddMenuDialog = false
                }
            )
        }
    }
}

@Composable
fun EnhancedAdminStatCard(
    title: String,
    count: String,
    icon: ImageVector,
    color: Color,
    bgColor: Color,
    isLoading: Boolean = false
) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon dengan background
            Card(
                modifier = Modifier.size(40.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = color
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Text section dengan layout yang lebih terorganisir
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLoading) "..." else count,
                    fontFamily = PoppinsFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLoading) Color(0xFF999999) else Color(0xFF1A1A1A)
                )
                Text(
                    text = title,
                    fontFamily = PoppinsFont,
                    fontSize = 10.sp,
                    color = Color(0xFF666666),
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }
        }
    }
}

@Composable
fun OrderManagementTab(
    orders: List<Order>,
    onOrderClick: (Order) -> Unit,
    onStatusChange: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders.sortedByDescending { it.createdAt?.seconds ?: 0 }) { order ->
            OrderCard(
                order = order,
                onClick = { onOrderClick(order) },
                onStatusChange = { newStatus -> onStatusChange(order.orderId, newStatus) }
            )
        }
    }
}

@Composable
fun MenuManagementTab(
    menuItems: List<CateringItem>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCB507)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier.size(32.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Tambah Menu Baru",
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        items(menuItems) { item ->
            MenuItemCard(
                item = item,
                onDeleteClick = { onDeleteClick(item.id ?: "") }
            )
        }
    }
}

@Composable
fun ChatManagementTab(
    navController: NavController
) {
    val viewModel: AdminViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    
    // Get unique users from orders
    val uniqueUsers = orders.map { order ->
        Triple(order.userId, order.senderName, "")
    }.distinctBy { it.first }.filter { it.first.isNotBlank() }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(48.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507)),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Chat dengan Pelanggan",
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A1A)
                        )
                        Text(
                            text = "${uniqueUsers.size} pelanggan tersedia untuk chat",
                            fontFamily = PoppinsFont,
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }
        }
        
        if (uniqueUsers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = Color(0xFFE0E0E0),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Belum ada pelanggan untuk chat",
                            color = Color(0xFF999999),
                            fontSize = 14.sp,
                            fontFamily = PoppinsFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Pelanggan akan muncul setelah ada pesanan",
                            color = Color(0xFFCCCCCC),
                            fontSize = 12.sp,
                            fontFamily = PoppinsFont,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(uniqueUsers) { user ->
                val (userId, userName, userPhotoUrl) = user
                ChatUserCard(
                    userId = userId,
                    userName = userName,
                    userPhotoUrl = userPhotoUrl,
                    onClick = {
                        // Navigate to the admin chat screen for the selected user
                        navController.navigate("admin_chat/$userId")
                    }
                )
            }
        }
    }
}

@Composable
fun ChatUserCard(
    userId: String,
    userName: String,
    userPhotoUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            if (userPhotoUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(userPhotoUrl),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Card(
                    modifier = Modifier.size(48.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507).copy(alpha = 0.1f)),
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.toString()?.uppercase() ?: "?",
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFFFCB507)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Pelanggan",
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            
            // Chat Icon
            Card(
                modifier = Modifier.size(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCB507).copy(alpha = 0.1f)),
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble,
                        contentDescription = null,
                        tint = Color(0xFFFCB507),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    onStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Order icon with background
                    Card(
                        modifier = Modifier.size(48.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (order.status) {
                                "pending" -> Color(0xFFFFF3E0)
                                "success" -> Color(0xFFE8F5E8)
                                "rejected" -> Color(0xFFFFEBEE)
                                else -> Color(0xFFF5F5F5)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (order.status) {
                                    "pending" -> Icons.Default.Schedule
                                    "success" -> Icons.Default.CheckCircle
                                    "rejected" -> Icons.Default.Cancel
                                    else -> Icons.Default.Receipt
                                },
                                contentDescription = null,
                                tint = when (order.status) {
                                    "pending" -> Color(0xFFFF9800)
                                    "success" -> Color(0xFF4CAF50)
                                    "rejected" -> Color(0xFFE53E3E)
                                    else -> Color(0xFF666666)
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Order #${order.orderId.take(8)}",
                            fontFamily = PoppinsFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = order.date,
                                fontFamily = PoppinsFont,
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💰",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rp ${String.format("%,.0f", order.total)}",
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFCB507)
                            )
                        }
                    }
                }
                
                EnhancedStatusChip(
                    status = order.status,
                    onStatusChange = onStatusChange
                )
            }
        }
    }
}

@Composable
fun EnhancedStatusChip(
    status: String,
    onStatusChange: (String) -> Unit
) {
    val statusConfig = when (status) {
        "pending" -> Triple(Color(0xFFFF9800), Color(0xFFFFF3E0), "Menunggu")
        "success" -> Triple(Color(0xFF4CAF50), Color(0xFFE8F5E8), "Berhasil")
        "rejected" -> Triple(Color(0xFFE53E3E), Color(0xFFFFEBEE), "Ditolak")
        else -> Triple(Color(0xFF666666), Color(0xFFF5F5F5), status)
    }
    
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        Card(
            modifier = Modifier
                .clickable { expanded = true }
                .shadow(2.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = statusConfig.second),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusConfig.third,
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusConfig.first
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .shadow(8.dp, RoundedCornerShape(12.dp))
        ) {
            DropdownMenuItem(
                text = { 
                    Text("Menunggu", fontFamily = PoppinsFont, color = Color(0xFFFF9800))
                },
                onClick = {
                    onStatusChange("pending")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { 
                    Text("Berhasil", fontFamily = PoppinsFont, color = Color(0xFF4CAF50))
                },
                onClick = {
                    onStatusChange("success")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { 
                    Text("Ditolak", fontFamily = PoppinsFont, color = Color(0xFFE53E3E))
                },
                onClick = {
                    onStatusChange("rejected")
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun MenuItemCard(
    item: CateringItem,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced item image
            Card(
                modifier = Modifier.size(70.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!item.imageUrl.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(item.imageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🍽️",
                                fontSize = 24.sp
                            )
                            Text(
                                text = item.category?.take(3)?.uppercase() ?: "MENU",
                                fontFamily = PoppinsFont,
                                fontSize = 8.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = item.category ?: "Umum",
                            fontFamily = PoppinsFont,
                            fontSize = 10.sp,
                            color = Color(0xFFE65100),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💰",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Rp ${String.format("%,.0f", item.price)}",
                        fontFamily = PoppinsFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCB507)
                    )
                }
            }
            
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDeleteClick() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailDialog(
    order: Order,
    onDismiss: () -> Unit,
    onStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) { },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Detail Pesanan",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = "#${order.orderId.take(8)}",
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                        
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF666666)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Order info cards
                    DetailInfoCard(
                        icon = Icons.Default.CalendarToday,
                        title = "Tanggal Pesanan",
                        value = order.date,
                        color = Color(0xFFFCB507)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetailInfoCard(
                        icon = Icons.Default.Person,
                        title = "Nama Pemesan",
                        value = order.senderName,
                        color = Color(0xFF2196F3)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetailInfoCard(
                        icon = Icons.Default.LocationOn,
                        title = "Alamat Pengiriman",
                        value = order.address,
                        color = Color(0xFFE65100)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetailInfoCard(
                        icon = Icons.Default.Note,
                        title = "Catatan",
                        value = order.note.ifEmpty { "Tidak ada catatan" },
                        color = Color(0xFFFF9800)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetailInfoCard(
                        icon = Icons.Default.Payment,
                        title = "Metode Pembayaran",
                        value = order.paymentMethod,
                        color = Color(0xFFE65100)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetailInfoCard(
                        icon = Icons.Default.AttachMoney,
                        title = "Total Pembayaran",
                        value = "Rp ${String.format("%,.0f", order.total)}",
                        color = Color(0xFFFCB507)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Current status
                    Text(
                        text = "Status Saat Ini",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    EnhancedStatusChip(
                        status = order.status,
                        onStatusChange = { /* No action in detail view */ }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Action buttons
                    Text(
                        text = "Ubah Status Pesanan",
                        fontFamily = PoppinsFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { 
                                onStatusChange("success")
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Terima",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Button(
                            onClick = { 
                                onStatusChange("rejected")
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Tolak",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(40.dp),
                colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = PoppinsFont,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AddMenuDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, Double, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUrl = it.toString()
        }
    }
    
    val categories = listOf(
        "daily-harian",
        "daily-mingguan", 
        "daily-bulanan",
        "event-wedding",
        "event-meeting",
        "event-selamatan",
        "event-gathering",
        "event-khusus"
    )

    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) { },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tambah Menu Baru",
                                fontFamily = PoppinsFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = "Isi detail menu yang akan ditambahkan",
                                fontFamily = PoppinsFont,
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                        
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF666666)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Form fields
                    EnhancedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Nama Menu",
                        icon = Icons.Default.Restaurant,
                        placeholder = "Contoh: Nasi Gudeg Komplit"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    EnhancedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Deskripsi",
                        icon = Icons.Default.Description,
                        placeholder = "Deskripsi menu...",
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    EnhancedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = "Harga",
                        icon = Icons.Default.AttachMoney,
                        placeholder = "25000"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category dropdown
                    Column {
                        Text(
                            text = "Kategori",
                            fontFamily = PoppinsFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Box {
                            OutlinedTextField(
                                value = category,
                                onValueChange = { },
                                placeholder = { 
                                    Text(
                                        "Pilih kategori menu", 
                                        fontFamily = PoppinsFont,
                                        color = Color(0xFF999999)
                                    ) 
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Category,
                                        contentDescription = null,
                                        tint = Color(0xFF666666),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        tint = Color(0xFF666666)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expandedCategory = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFCB507),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedLabelColor = Color(0xFFFCB507)
                                ),
                                readOnly = true
                            )
                            
                            DropdownMenu(
                                expanded = expandedCategory,
                                onDismissRequest = { expandedCategory = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                categories.forEach { categoryItem ->
                                    DropdownMenuItem(
                                        text = { Text(categoryItem, fontFamily = PoppinsFont) },
                                        onClick = {
                                            category = categoryItem
                                            expandedCategory = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Image upload section
                    Column {
                        Text(
                            text = "Gambar Menu (Opsional)",
                            fontFamily = PoppinsFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clickable { 
                                    imagePickerLauncher.launch("image/*")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageUrl != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUrl),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudUpload,
                                            contentDescription = null,
                                            tint = Color(0xFF999999),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Pilih Gambar",
                                            fontFamily = PoppinsFont,
                                            fontSize = 14.sp,
                                            color = Color(0xFF999999)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Batal",
                                fontFamily = PoppinsFont,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Button(
                            onClick = {
                                if (title.isNotBlank() && description.isNotBlank() && 
                                    price.isNotBlank() && category.isNotBlank()) {
                                    try {
                                        val priceValue = price.toDouble()
                                        onAdd(title, description, priceValue, category, imageUrl)
                                    } catch (e: NumberFormatException) {
                                        // Handle invalid price
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Tambah",
                                fontFamily = PoppinsFont,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    placeholder: String,
    maxLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { 
                Text(
                    placeholder, 
                    fontFamily = PoppinsFont,
                    color = Color(0xFF999999)
                ) 
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFCB507),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = Color(0xFFFCB507)
            ),
            maxLines = maxLines
        )
    }
}

fun formatRupiah(amount: Double): String {
    val formatter = java.text.NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount)
}
