package com.example.foca.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foca.data.model.ChatMessage
import com.example.foca.data.viewmodel.ChatViewModel
import com.example.foca.data.viewmodel.ProfileViewModel
import com.example.foca.ui.theme.PoppinsFont
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChatScreen(
    navController: NavController,
    userId: String, // user yang sedang dipilih admin
    adminId: String = "admin" // ID unik untuk admin
) {
    val chatViewModel: ChatViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val chatMessages by chatViewModel.chatMessages.collectAsState()
    val adminProfile by profileViewModel.profile.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberScrollState()
    val userProfile by profileViewModel.userProfile.collectAsState()


    // Load profile dan chat saat pertama kali
    LaunchedEffect(userId) {
        profileViewModel.loadProfile(adminId)
        profileViewModel.loadUserProfile(userId)
        chatViewModel.listenChatMessages(userId, adminId)
    }

    // Scroll otomatis ke bawah saat ada pesan baru
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollTo(listState.maxValue)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // User Avatar
                        val photoUrl = userProfile?.photoUrl ?: ""
                        val name = userProfile?.firstName ?: "Pengguna"
                        if (photoUrl.isNotBlank()) {
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = "User Photo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFBDBDBD), shape = RoundedCornerShape(50))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.firstOrNull()?.uppercase() ?: "P",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = PoppinsFont
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = name,
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDFBF7),
                    titleContentColor = Color(0xFF1A1A1A)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFFDFBF7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFBF7))
                .padding(paddingValues)
        ) {
            // Chat area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(listState)
                        .padding(4.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (chatMessages.isEmpty()) {
                        Text(
                            text = "Mulai percakapan dengan pengguna.",
                            color = Color.Gray,
                            fontFamily = PoppinsFont,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        chatMessages.forEach { message ->
                            UnifiedChatBubble(
                                message = message,
                                isCurrentUser = message.sender == adminId
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            // Input bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ketik pesan...", fontFamily = PoppinsFont, color = Color(0xFF999999)) },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFCB507),
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFFFCB507)
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = PoppinsFont, fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (inputText.text.isNotBlank()) {
                            val photoUrl = adminProfile?.photoUrl ?: ""
                            chatViewModel.sendMessage(userId, inputText.text, adminId, photoUrl)
                            inputText = TextFieldValue("")
                        }
                    },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB507)),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun UnifiedChatBubble(message: ChatMessage, isCurrentUser: Boolean) {
    val time = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCurrentUser) Color(0xFFFCB507) else Color(0xFFF1F0F0),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCurrentUser) 16.dp else 0.dp,
                            bottomEnd = if (isCurrentUser) 0.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isCurrentUser) Color.White else Color.Black,
                    fontFamily = PoppinsFont,
                    fontSize = 15.sp
                )
            }
            Text(
                text = time,
                color = Color.Gray,
                fontFamily = PoppinsFont,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp, end = 4.dp, start = 4.dp)
            )
        }
    }
}
