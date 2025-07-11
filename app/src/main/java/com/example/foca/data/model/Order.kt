package com.example.foca.data.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val senderName: String = "",
    val address: String = "",
    val note: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val paymentMethod: String = "QRIS",
    val status: String = "pending", // "pending", "success", "rejected"
    val items: List<String> = emptyList(), // List of item IDs
    val createdAt: com.google.firebase.Timestamp? = null,
    val updatedAt: com.google.firebase.Timestamp? = null
) 