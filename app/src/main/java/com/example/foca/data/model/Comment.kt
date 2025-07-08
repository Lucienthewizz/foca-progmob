package com.example.foca.data.model

data class Comment(
    val commentId: String = "",
    val itemId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val text: String = "",
    val rating: Int = 0,
    val timestamp: Long = 0L
) 