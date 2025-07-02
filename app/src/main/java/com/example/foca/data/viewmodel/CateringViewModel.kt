package com.example.foca.data.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.CateringItem
import com.example.foca.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CateringViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery
    
    private val _cateringItems = MutableStateFlow<List<CateringItem>>(emptyList())
    val cateringItems: StateFlow<List<CateringItem>> = _cateringItems.asStateFlow()
    
    private val _recommendedItems = MutableStateFlow<List<CateringItem>>(emptyList())
    val recommendedItems: StateFlow<List<CateringItem>> = _recommendedItems.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _allItems = MutableStateFlow<List<CateringItem>>(emptyList())
    val allItems: StateFlow<List<CateringItem>> = _allItems.asStateFlow()
    
    private val _recommendedMenus = MutableStateFlow<List<CateringItem>>(emptyList())
    val recommendedMenus: StateFlow<List<CateringItem>> = _recommendedMenus.asStateFlow()
    private val _favoriteMenus = MutableStateFlow<List<CateringItem>>(emptyList())
    val favoriteMenus: StateFlow<List<CateringItem>> = _favoriteMenus.asStateFlow()
    
    init {
        loadRecommendedItems()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun loadCateringItemsByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getCateringItemsByCategory(category).collect { items ->
                    _cateringItems.value = items
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadRecommendedItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = repository.getRecommendedItems()
                _recommendedItems.value = items
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
    
    fun loadAllCateringItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = repository.getAllCateringItems()
                _allItems.value = items
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
    
    fun loadRecommendedMenus() {
        viewModelScope.launch {
            repository.getRecommendedMenus().collect { items ->
                _recommendedMenus.value = items
            }
        }
    }
    
    fun loadFavoriteMenus() {
        viewModelScope.launch {
            repository.getFavoriteMenus().collect { items ->
                _favoriteMenus.value = items
            }
        }
    }
    
    fun addToCart(userId: String, cateringId: String, onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repository.addToCart(userId, cateringId)
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e)
            }
        }
    }
}