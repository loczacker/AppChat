package com.rikkei.training.appchat.ui.splash

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.training.appchat.databinding.ActivitySplashBinding
import com.rikkei.training.appchat.ui.home.HomeActivity
import com.rikkei.training.appchat.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splash.alpha = 0f
        binding.splash.animate().setDuration(1500).alpha(1f).withEndAction {
            if (FirebaseAuth.getInstance().currentUser?.uid != null){
                startActivity(Intent(this, HomeActivity::class.java))
                Intent.FLAG_ACTIVITY_NEW_TASK
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
        textOneSpan()

    }

    private fun textOneSpan() {
        val spannableString = SpannableString("Awesome chat")
        val styleSpan = StyleSpan(Typeface.BOLD_ITALIC )
        spannableString.setSpan(styleSpan,0,7,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.tvAwesomeChat.text = spannableString
    }



}