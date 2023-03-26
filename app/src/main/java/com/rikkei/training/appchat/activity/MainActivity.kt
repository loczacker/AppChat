package com.rikkei.training.appchat.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rikkei.training.appchat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startSplash()

}

    private fun startSplash() {
        val intentSplashActivity = Intent(this, SplashActivity::class.java)
        startActivity(intentSplashActivity)
    }
}