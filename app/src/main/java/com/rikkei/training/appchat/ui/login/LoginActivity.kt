package com.rikkei.training.appchat.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.ui.home.HomeActivity
import com.rikkei.training.appchat.ui.register.RegisterActivity
import com.rikkei.training.appchat.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textOneSpan()
        changeColor()

        binding.tvRegisterLogin.setOnClickListener{
            startSignUp()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        //login firebase
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                        startHome(email,password)
            } else {
                binding.btnLogin.visibility = View.VISIBLE
            }

        }

        binding.edEmail.addTextChangedListener()

    }

    private fun textOneSpan() {
        val spannableString = SpannableString("Chưa có tài khoản? Đăng ký ngay")
        val fColor = ForegroundColorSpan(Color.BLUE)
        spannableString.setSpan(fColor,18,31, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.tvRegisterLogin.text = spannableString
    }

    private fun startSignUp() {
        val intentSignUp = Intent(this, RegisterActivity::class.java)
        startActivity(intentSignUp)
    }


    //Change Color
    private fun changeColor() {
        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                ) {
                    binding.btnLogin.setBackgroundResource(R.drawable.buttonblue)
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.button_white)
                }
            }
        })
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                ) {
                    binding.btnLogin.setBackgroundResource(R.drawable.buttonblue)
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.button_white)
                }
            }
        })
    }

    private fun startHome(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = Firebase.auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Log.e(TAG, task.exception.toString())
                    updateUI(null)
                }
            }
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }

}