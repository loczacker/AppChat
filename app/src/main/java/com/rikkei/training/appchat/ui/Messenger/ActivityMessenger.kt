package com.rikkei.training.appchat.ui.Messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityMessengerBinding
import com.rikkei.training.appchat.ui.home.HomeActivity


class ActivityMessenger : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        database.reference.child("Users")
            .child(firebaseAuth.uid?:"").child("presence").setValue("Online")
        infoUserChat()
        backHome()
        sendMessenger(String(), String(), toString())
    }

    private fun sendMessenger(senderId: String, receiverId: String, messenger: String) {
        binding.ivSend.setOnClickListener{

        }
    }

    private fun backHome() {
        binding.imBackHome.setOnClickListener{
            val homeIntent = Intent(this, HomeActivity::class.java)
            homeIntent.putExtra("currentFragment", "FragmentFriends")
            startActivity(homeIntent)
            finish()
        }
    }

    private fun infoUserChat() {
        val name = intent.getStringExtra("name")
        val imgProfile = intent.getStringExtra("img")
        val uidUser = intent.getStringExtra("uid")
        binding.tvNameMess.text = name
        Glide.with(this@ActivityMessenger)
            .load(imgProfile)
            .transform(CenterCrop(), RoundedCorners(55))
            .placeholder(R.drawable.profile)
            .into(binding.ivImgProfile)
        database.reference.child("Users").child(uidUser.toString()).child("presence")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvPresence.text = snapshot.value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onPause() {
        super.onPause()
        database.reference.child("Users")
            .child(firebaseAuth.uid?:"").child("presence").setValue("Offline")
    }

}