package com.rikkei.training.appchat.vm

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rikkei.training.appchat.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textOneSpan()
        textTwoSpan()

        binding.tvLoginRegister.setOnClickListener{
            loginBack()
        }

        binding.ibRes.setOnClickListener{
            loginBack()
        }

        binding.cb1.setOnClickListener{

        }

        binding.btRes.setOnClickListener {
            register("abc@gmail.com","12345678")
        }

    }

    private fun textOneSpan() {
        val spannableString = SpannableString("Đã có tài khoản? Đăng nhập ngay")
        val fColor = ForegroundColorSpan(Color.BLUE)
        spannableString.setSpan(fColor,16,31, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.tvLoginRegister.text = spannableString
    }

    private fun textTwoSpan() {
        val spannableString = SpannableString("Tôi đồng ý với các chính sách và điều khoản")
        val fColor = ForegroundColorSpan(Color.BLUE)
        spannableString.setSpan(fColor, 19, 30, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val bColor = ForegroundColorSpan(Color.BLUE)
        spannableString.setSpan(bColor, 33,43, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.tvAgree.text = spannableString
    }

    private fun loginBack() {
        val intentLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(intentLoginActivity)
    }


    private  fun register(email: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AHIHIHIH", "createUserWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AHIHIHIH", "createUserWithEmail:failure", task.exception)
                }
            }

    }
}