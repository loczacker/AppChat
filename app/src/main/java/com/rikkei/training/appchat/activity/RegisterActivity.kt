package com.rikkei.training.appchat.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.Users
import com.rikkei.training.appchat.databinding.ActivityRegisterBinding
import com.rikkei.training.appchat.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "RegisterActivity"
    }
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        textOneSpan()
        textTwoSpan()
        changeColor()
        //Textview back login
        binding.tvLoginRegister.setOnClickListener{
            loginBack()
        }
        //ImageButton back login
        binding.ibRes.setOnClickListener{
            loginBack()
        }

        //validate signup firebase

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btRes.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val checkPolicy = binding.cb1

            val userId = databaseReference.push().key!!
            val users = Users(email, name)

            databaseReference.child(email).setValue(userId)
                .addOnCompleteListener{
                    Toast.makeText(this, "Data insert thanh cong", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()

                }

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && checkPolicy.isChecked) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        if (password.length >= 8
                            && password.matches(".*[A-Z].*".toRegex())
                            && password.matches(".*[a-z].*".toRegex())
                            && password.matches(".*\\d.*".toRegex())) {
                            register(email,password)


                        } else {

                            Toast.makeText(this, "Password is not invalid", Toast.LENGTH_SHORT).show()

                        }
                    } else

                        Toast.makeText(this, "Email is not invalid", Toast.LENGTH_SHORT).show()


            } else {
            }
        }

    }

    //Change Color
    private fun changeColor() {
        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                }
            }
        })
        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                }
            }
        })
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (binding.edName.text!!.isNotEmpty()
                    && binding.edEmail.text!!.isNotEmpty()
                    && binding.edPassword.text!!.isNotEmpty()
                    && binding.cb1.isChecked
                ) {
                    binding.btRes.setBackgroundResource(R.drawable.buttonblue)
                } else {
                    binding.btRes.setBackgroundResource(R.drawable.button_white)
                }
            }
        })
        binding.cb1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, _ ->
            if (binding.edName.text!!.isNotEmpty()
                && binding.edEmail.text!!.isNotEmpty()
                && binding.edPassword.text!!.isNotEmpty()
                && binding.cb1.isChecked
            ) {
                binding.btRes.setBackgroundResource(R.drawable.buttonblue)
            } else {
                binding.btRes.setBackgroundResource(R.drawable.buttonblue)
            }
        })
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
                    Log.d(TAG, "createUserWithEmail:success")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Error not create user firebase", Toast.LENGTH_SHORT).show()
                }
            }

    }
}