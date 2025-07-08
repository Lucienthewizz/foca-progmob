package com.example.foca.data.model

// Model for chat messages

data class ChatMessage(
    val sender: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val photoUrl: String = ""
) 