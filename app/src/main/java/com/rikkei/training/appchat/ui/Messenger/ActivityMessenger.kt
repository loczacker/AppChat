package com.rikkei.training.appchat.ui.Messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.databinding.ActivityMessengerBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.home.HomeActivity

class ActivityMessenger : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.imBackHome.setOnClickListener{
            backHome()
        }
    }


    private fun backHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

}