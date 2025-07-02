package com.example.foca.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.CartItem
import com.example.foca.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _operationStatus = MutableStateFlow<OperationStatus?>(null)
    val operationStatus: StateFlow<OperationStatus?> = _operationStatus.asStateFlow()

    sealed class OperationStatus {
        data class Success(val message: String) : OperationStatus()
        data class Error(val message: String) : OperationStatus()
    }

    fun loadCartItems(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val items = repository.getCartItemsByUserId(userId)
            _cartItems.value = items
            _isLoading.value = false
        }
    }

    fun updateCartItemQuantity(userId: String, cateringId: String, quantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (quantity <= 0) {
                    removeCartItem(userId, cateringId)
                    return@launch
                }
                repository.updateCartItemQuantity(userId, cateringId, quantity)
                loadCartItems(userId)
                _operationStatus.value = OperationStatus.Success("Jumlah item berhasil diperbarui")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Gagal memperbarui jumlah item: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeCartItem(userId: String, cateringId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.removeCartItem(userId, cateringId)
                loadCartItems(userId)
                _operationStatus.value = OperationStatus.Success("Item berhasil dihapus dari keranjang")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Gagal menghapus item: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.clearCart(userId)
                loadCartItems(userId)
                _operationStatus.value = OperationStatus.Success("Keranjang berhasil dikosongkan")
            } catch (e: Exception) {
                _operationStatus.value = OperationStatus.Error("Gagal mengosongkan keranjang: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetOperationStatus() {
        _operationStatus.value = null
    }
}