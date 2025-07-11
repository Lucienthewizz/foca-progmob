package com.example.foca.data.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.UserProfile
import com.example.foca.data.model.Order
import com.example.foca.data.repository.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Log

class ProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()
    private val repository = FirebaseRepository()

    fun loadProfile(userId: String) {
        Log.d("ProfileViewModel", "Loading profile for user: $userId")
        viewModelScope.launch {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val profile = doc.toObject(UserProfile::class.java)
                        Log.d("ProfileViewModel", "Profile loaded: ${profile?.email}, role: ${profile?.role}")
                        _profile.value = profile
                    } else {
                        Log.d("ProfileViewModel", "User document not found for: $userId")
                        _profile.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfileViewModel", "Error loading profile for: $userId", exception)
                    _profile.value = null
                }
        }
    }

    fun loadUserProfile(userId: String) {
        Log.d("ProfileViewModel", "Loading user profile for: $userId")
        viewModelScope.launch {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val user = doc.toObject(UserProfile::class.java)
                        Log.d("ProfileViewModel", "User profile loaded: ${user?.email}")
                        _userProfile.value = user
                    } else {
                        Log.d("ProfileViewModel", "User document not found for: $userId")
                        _userProfile.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfileViewModel", "Error loading user profile for: $userId", exception)
                    _userProfile.value = null
                }
        }
    }

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            val result = repository.getOrdersByUserId(userId)
            _orders.value = result
        }
    }

    fun updateProfile(uid: String, firstName: String, lastName: String, email: String, phone: String) {
        viewModelScope.launch {
            val profileUpdate = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "name" to "$firstName $lastName".trim(),
                "email" to email,
                "phone" to phone
            )
            firestore.collection("users").document(uid)
                .update(profileUpdate as Map<String, Any>)
                .addOnSuccessListener {
                    // Refresh profile
                    loadProfile(uid)
                }
        }
    }

    fun uploadProfilePhoto(userId: String, imageUri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        _isUploading.value = true
        val storageRef = storage.reference
        val imageRef = storageRef.child("profile_photos/${userId}/${UUID.randomUUID()}.jpg")
        
        imageRef.putFile(imageUri)
            .addOnSuccessListener { 
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Update profile with new photo URL
                    firestore.collection("users").document(userId)
                        .update("photoUrl", downloadUrl.toString())
                        .addOnSuccessListener {
                            _isUploading.value = false
                            loadProfile(userId) // Refresh profile
                            onSuccess(downloadUrl.toString())
                        }
                        .addOnFailureListener { e ->
                            _isUploading.value = false
                            onError("Gagal memperbarui profil: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                _isUploading.value = false
                onError("Gagal mengunggah foto: ${e.message}")
            }
    }
}