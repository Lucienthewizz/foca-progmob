package com.example.foca

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.foca.ui.MainApp
import com.example.foca.ui.theme.FocaTheme
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            var userId by remember { mutableStateOf<String?>(null) }
            
            // Listen to authentication state changes only
            LaunchedEffect(Unit) {
                val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                    val currentUser = auth.currentUser
                    userId = currentUser?.uid
                }
                firebaseAuth.addAuthStateListener(authStateListener)
            }
            
            FocaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(
                        onGoogleSignIn = { launchGoogleSignIn { uid -> userId = uid } },
                        userId = userId,
                        onLogout = { 
                            // Sign out from Firebase first
                            firebaseAuth.signOut()
                            // Clear "Remember Me" preference
                            val sharedPref = getSharedPreferences("foca_prefs", Context.MODE_PRIVATE)
                            sharedPref.edit().putBoolean("remember_me", false).apply()
                            // Clear Google Sign-In
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                            val googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)
                            googleSignInClient.signOut()
                            // Finally clear userId
                            userId = null
                        }
                    )
                }
            }
        }
    }

    // Launcher for Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account) { uid ->
                // Set userId in Compose state
                runOnUiThread { setUserIdInCompose(uid) }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private var setUserIdInCompose: ((String?) -> Unit) = {}
    private fun launchGoogleSignIn(onUserId: (String?) -> Unit) {
        setUserIdInCompose = onUserId
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?, onUserId: (String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    // Save/update user profile to Firestore
                    user?.let {
                        val profileData = hashMapOf(
                            "userId" to it.uid,
                            "firstName" to (it.displayName?.split(" ")?.getOrNull(0) ?: ""),
                            "lastName" to (it.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""),
                            "name" to (it.displayName ?: ""),
                            "email" to (it.email ?: ""),
                            "photoUrl" to (it.photoUrl?.toString() ?: ""),
                            "phone" to "",
                            "birthDate" to "",
                            "role" to if (it.email?.lowercase()?.trim() == "admin@foca.com") "admin" else "user", // Set admin role for specific email
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )
                        firestore.collection("users").document(it.uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // User already exists, update role and photo if needed
                                    val updateData = hashMapOf(
                                        "photoUrl" to (it.photoUrl?.toString() ?: ""),
                                        "role" to if (it.email?.lowercase()?.trim() == "admin@foca.com") "admin" else "user" // Always check and update role
                                    )
                                    firestore.collection("users").document(it.uid).update(updateData as Map<String, Any>)
                                } else {
                                    // Create new user profile
                                    firestore.collection("users").document(it.uid).set(profileData)
                                }
                            }
                        // Log login event
                        val loginLog = hashMapOf(
                            "userId" to it.uid,
                            "email" to (it.email ?: ""),
                            "timestamp" to com.google.firebase.Timestamp.now(),
                            "method" to "google"
                        )
                        firestore.collection("login_history").add(loginLog)
                    }
                    Toast.makeText(this, "Login Google Berhasil!", Toast.LENGTH_SHORT).show()
                    
                    // Save "Remember Me" preference as true for Google Sign-In
                    val sharedPref = getSharedPreferences("foca_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putBoolean("remember_me", true).apply()
                    
                    onUserId(user?.uid)
                } else {
                    Toast.makeText(this, "Firebase Auth failed.", Toast.LENGTH_SHORT).show()
                    onUserId(null)
                }
            }
    }
}