package com.example.foca.data.model

data class CateringItem(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val isRecommended: Boolean = false,
    val isFavorite: Boolean = false,
    val rating: Double? = 0.0
)