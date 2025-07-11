package com.example.foca.data.model

// Model for user profile

data class UserProfile(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val photoUrl: String = "",
    val role: String = "user", // "user" or "admin"
    val createdAt: com.google.firebase.Timestamp? = null
)