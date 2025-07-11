package com.example.foca.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.CateringItem
import com.example.foca.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    
    private val _menuItems = MutableStateFlow<List<CateringItem>>(emptyList())
    val menuItems: StateFlow<List<CateringItem>> = _menuItems.asStateFlow()

    fun loadAllOrders() {
        viewModelScope.launch {
            firestore.collection("orders")
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.let { querySnapshot ->
                        val ordersList = querySnapshot.documents.mapNotNull { doc ->
                            doc.toObject(Order::class.java)?.copy(orderId = doc.id)
                        }
                        _orders.value = ordersList
                    }
                }
        }
    }

    fun loadAllMenuItems() {
        viewModelScope.launch {
            firestore.collection("catering_items")
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.let { querySnapshot ->
                        val itemsList = querySnapshot.documents.mapNotNull { doc ->
                            doc.toObject(CateringItem::class.java)?.copy(id = doc.id)
                        }
                        _menuItems.value = itemsList
                    }
                }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            val updateData = hashMapOf(
                "status" to newStatus,
                "updatedAt" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("orders").document(orderId)
                .update(updateData as Map<String, Any>)
        }
    }

    fun addMenuItem(title: String, description: String, price: Double, category: String, imageUrl: String? = null) {
        viewModelScope.launch {
            val newItem = hashMapOf(
                "title" to title,
                "description" to description,
                "price" to price,
                "category" to category,
                "rating" to 5.0,
                "imageUrl" to (imageUrl ?: ""),
                "createdAt" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("catering_items").add(newItem)
        }
    }

    fun deleteMenuItem(itemId: String) {
        viewModelScope.launch {
            firestore.collection("catering_items").document(itemId).delete()
        }
    }
}
