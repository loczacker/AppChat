package com.rikkei.training.appchat.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.rikkei.training.appchat.databinding.ActivitySplashBinding

private lateinit var binding: ActivitySplashBinding



class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splash.alpha = 0f
        binding.splash.animate().setDuration(1500).alpha(1f).withEndAction(){
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

        }
        textOneSpan()
        //setting splash

    }

    //setting 2 form with awesome chat
    private fun textOneSpan() {
        val spannableString = SpannableString("Awesome chat")
        val styleSpan = StyleSpan(Typeface.BOLD_ITALIC )
        spannableString.setSpan(styleSpan,0,7,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.tvAwesomeChat.text = spannableString
    }



}