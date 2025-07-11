package com.example.foca.data.repository

import com.example.foca.data.model.CateringItem
import com.example.foca.data.model.CartItem
import com.example.foca.data.model.Order
import com.example.foca.data.model.Comment
import com.example.foca.data.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val cateringCollection = firestore.collection("catering_items")
    private val commentsCollection = firestore.collection("comments")
    
    fun getCateringItemsByCategory(category: String): Flow<List<CateringItem>> = callbackFlow {
        val snapshotListener = cateringCollection
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { document ->
                        document.toObject(CateringItem::class.java)?.copy(id = document.id)
                    }
                    trySend(items)
                }
            }
        
        awaitClose { snapshotListener.remove() }
    }
    
    suspend fun getAllCateringItems(): List<CateringItem> {
        return try {
            val snapshot = cateringCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(CateringItem::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getRecommendedItems(limit: Int = 5): List<CateringItem> {
        return try {
            val snapshot = cateringCollection.limit(limit.toLong()).get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(CateringItem::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCartItemsByUserId(userId: String): List<CartItem> {
        return try {
            val snapshot = firestore.collection("cart")
                .whereEqualTo("userId", userId)
                .get().await()
            if (snapshot.documents.isNotEmpty()) {
                val items = snapshot.documents[0].get("items") as? List<Map<String, Any>>
                items?.map {
                    CartItem(
                        cateringId = it["cateringId"] as? String ?: "",
                        quantity = (it["quantity"] as? Long)?.toInt() ?: 0
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getRecommendedMenus(): Flow<List<CateringItem>> = callbackFlow {
        val snapshotListener = cateringCollection
            .whereEqualTo("isRecommended", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { document ->
                        document.toObject(CateringItem::class.java)?.copy(id = document.id)
                    }
                    trySend(items)
                }
            }
        awaitClose { snapshotListener.remove() }
    }

    fun getFavoriteMenus(): Flow<List<CateringItem>> = callbackFlow {
        val snapshotListener = cateringCollection
            .whereEqualTo("isFavorite", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { document ->
                        document.toObject(CateringItem::class.java)?.copy(id = document.id)
                    }
                    trySend(items)
                }
            }
        awaitClose { snapshotListener.remove() }
    }

    suspend fun addToCart(userId: String, cateringId: String) {
        val cartRef = firestore.collection("cart").document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(cartRef)
            val items = if (snapshot.exists()) {
                val currentItems = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                val mutableItems = currentItems.toMutableList()
                val index = mutableItems.indexOfFirst { it["cateringId"] == cateringId }
                if (index >= 0) {
                    val item = mutableItems[index]
                    val qty = (item["quantity"] as? Long ?: 0L) + 1L
                    mutableItems[index] = mapOf("cateringId" to cateringId, "quantity" to qty)
                } else {
                    mutableItems.add(mapOf("cateringId" to cateringId, "quantity" to 1L))
                }
                mutableItems
            } else {
                listOf(mapOf("cateringId" to cateringId, "quantity" to 1L))
            }
            transaction.set(cartRef, mapOf("userId" to userId, "items" to items))
        }.await()
    }
    
    suspend fun updateCartItemQuantity(userId: String, cateringId: String, quantity: Int) {
        val cartRef = firestore.collection("cart").document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(cartRef)
            if (snapshot.exists()) {
                val currentItems = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                val mutableItems = currentItems.toMutableList()
                val index = mutableItems.indexOfFirst { it["cateringId"] == cateringId }
                if (index >= 0) {
                    mutableItems[index] = mapOf("cateringId" to cateringId, "quantity" to quantity.toLong())
                    transaction.set(cartRef, mapOf("userId" to userId, "items" to mutableItems))
                }
            }
        }.await()
    }
    
    suspend fun removeCartItem(userId: String, cateringId: String) {
        val cartRef = firestore.collection("cart").document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(cartRef)
            if (snapshot.exists()) {
                val currentItems = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                val mutableItems = currentItems.toMutableList()
                val index = mutableItems.indexOfFirst { it["cateringId"] == cateringId }
                if (index >= 0) {
                    mutableItems.removeAt(index)
                    transaction.set(cartRef, mapOf("userId" to userId, "items" to mutableItems))
                }
            }
        }.await()
    }
    
    suspend fun clearCart(userId: String) {
        val cartRef = firestore.collection("cart").document(userId)
        cartRef.set(mapOf("userId" to userId, "items" to emptyList<Map<String, Any>>())).await()
    }

    suspend fun saveOrder(order: Order): String? {
        return try {
            val orderWithPendingStatus = order.copy(
                status = "pending",
                createdAt = com.google.firebase.Timestamp.now()
            )
            val docRef = firestore.collection("orders").add(orderWithPendingStatus).await()
            docRef.id
        } catch (e: Exception) {
            null
        }
    }


    fun getCommentsByItemId(itemId: String): Flow<List<Comment>> = callbackFlow {
        val listener = commentsCollection
            .whereEqualTo("itemId", itemId)
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val comments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Comment::class.java)?.copy(commentId = doc.id)
                    }
                    trySend(comments)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addComment(comment: Comment) {
        val docRef = commentsCollection.document()
        val commentWithId = comment.copy(commentId = docRef.id)
        docRef.set(commentWithId).await()
    }

    fun getLatestComments(limit: Int = 10): Flow<List<Comment>> = callbackFlow {
        val listener = commentsCollection
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val comments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Comment::class.java)?.copy(commentId = doc.id)
                    }
                    trySend(comments)
                }
            }
        awaitClose { listener.remove() }
    }

    // --- CHAT ADMIN ---
    fun getChatMessages(userId: String): Flow<List<ChatMessage>> = callbackFlow {
        val messagesCollection = firestore.collection("chat").document(userId).collection("messages")
        val listener = messagesCollection
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ChatMessage::class.java)
                    }
                    trySend(messages)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendChatMessage(userId: String, message: ChatMessage) {
        val messagesCollection = firestore.collection("chat").document(userId).collection("messages")
        messagesCollection.add(message).await()
    }

    // --- ORDERS ---
    suspend fun getOrdersByUserId(userId: String): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Order::class.java)?.copy(orderId = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createOrder(order: Order): String? {
        return try {
            val orderWithPendingStatus = order.copy(
                status = "pending",
                createdAt = com.google.firebase.Timestamp.now()
            )
            val docRef = firestore.collection("orders").add(orderWithPendingStatus).await()
            docRef.id
        } catch (e: Exception) {
            null
        }
    }
}