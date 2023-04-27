package com.rikkei.training.appchat.ui.Messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rikkei.training.appchat.databinding.ActivityMessengerBinding
import com.rikkei.training.appchat.ui.home.HomeActivity
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class ActivityMessenger : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding
    private var listener: ItemUsersRecycleView? = null



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
        finish()
    }

}