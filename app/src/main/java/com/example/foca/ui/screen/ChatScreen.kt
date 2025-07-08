package com.example.foca.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foca.data.viewmodel.ChatViewModel
import com.example.foca.data.model.ChatMessage
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.*
import coil.compose.AsyncImage
import com.example.foca.data.viewmodel.ProfileViewModel

@Composable
fun ChatScreen(navController: NavController, userId: String = "user1", sender: String = "user") {
    val chatViewModel: ChatViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val chatMessages by chatViewModel.chatMessages.collectAsState()
    val newAdminMessage by chatViewModel.newAdminMessage.collectAsState()
    val userProfile by profileViewModel.profile.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberScrollState()

    // Load profile saat pertama kali
    LaunchedEffect(userId) {
        profileViewModel.loadProfile(userId)
        chatViewModel.listenChatMessages(userId, sender)
    }

    // Scroll otomatis ke bawah saat ada pesan baru
    LaunchedEffect(chatMessages.size) {
        listState.scrollTo(listState.maxValue)
    }

    // Notifikasi pesan baru dari admin
    LaunchedEffect(newAdminMessage) {
        newAdminMessage?.let {
            snackbarHostState.showSnackbar("Pesan baru dari admin: ${it.text}")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFFDFBF7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Chat Admin",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(listState),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (chatMessages.isEmpty()) {
                        Text(
                            text = "Belum ada pesan.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        chatMessages.forEach { message ->
                            ChatBubble(
                                message = message,
                                isUser = message.sender == sender
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ketik pesan...") },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (inputText.text.isNotBlank()) {
                            val photoUrl = userProfile?.photoUrl ?: ""
                            chatViewModel.sendMessage(userId, inputText.text, sender, photoUrl)
                            inputText = TextFieldValue("")
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text("Kirim")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, isUser: Boolean) {
    val initials = if (message.sender.isNotBlank()) message.sender.first().uppercaseChar().toString() else "?"
    val time = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Avatar admin di kiri
            AvatarCircle(initials, message.photoUrl)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isUser) Color(0xFFDCF8C6) else Color(0xFFF1F0F0),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .widthIn(max = 240.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Text(
                text = time,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp, end = 4.dp, start = 4.dp)
            )
        }
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // Avatar user di kanan
            AvatarCircle(initials, message.photoUrl)
        }
    }
}

@Composable
fun AvatarCircle(initials: String, photoUrl: String) {
    if (photoUrl.isNotBlank()) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFBDBDBD), shape = RoundedCornerShape(50))
        )
    } else {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFBDBDBD), shape = RoundedCornerShape(50))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}