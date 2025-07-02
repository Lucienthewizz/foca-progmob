package com.example.foca.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.UserProfile
import com.example.foca.data.model.Order
import com.example.foca.data.repository.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    private val repository = FirebaseRepository()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            firestore.collection("profile").document(userId).get()
                .addOnSuccessListener { doc ->
                    _profile.value = doc.toObject(UserProfile::class.java)
                }
                .addOnFailureListener {
                    _profile.value = null
                }
        }
    }

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            val result = repository.getOrdersByUserId(userId)
            _orders.value = result
        }
    }
} 