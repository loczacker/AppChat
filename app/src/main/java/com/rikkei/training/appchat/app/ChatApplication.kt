package com.rikkei.training.appchat.app

import android.app.Application
import com.google.firebase.FirebaseApp

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}