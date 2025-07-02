package com.example.foca.data.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val address: String = "",
    val note: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val paymentMethod: String = "QRIS",
    val status: String = "Sukses"
) 