package com.example.foca.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foca.data.model.ChatMessage
import com.example.foca.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _newAdminMessage = MutableStateFlow<ChatMessage?>(null)
    val newAdminMessage: StateFlow<ChatMessage?> = _newAdminMessage.asStateFlow()

    fun listenChatMessages(userId: String, sender: String) {
        viewModelScope.launch {
            repository.getChatMessages(userId).collectLatest { messages ->
                // Deteksi pesan baru dari admin
                val lastMessage = messages.lastOrNull()
                if (lastMessage != null && lastMessage.sender != sender) {
                    _newAdminMessage.value = lastMessage
                }
                _chatMessages.value = messages
            }
        }
    }

    fun sendMessage(userId: String, text: String, sender: String, photoUrl: String) {
        viewModelScope.launch {
            val message = ChatMessage(
                sender = sender,
                text = text,
                timestamp = System.currentTimeMillis(),
                photoUrl = photoUrl
            )
            repository.sendChatMessage(userId, message)
        }
    }
} 