package com.example.foca

import android.app.Application
import com.google.firebase.FirebaseApp

class FocaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}