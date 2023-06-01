package com.rikkei.training.appchat.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.ui.tabFriends.TabFriendsFragment
import com.rikkei.training.appchat.ui.roomMessage.RoomMessageFragment
import com.rikkei.training.appchat.ui.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val roomId = intent.getStringExtra("roomId")
        val uidFriend = intent.getStringExtra("uidFriend")
        print(roomId)
        print(uidFriend)
        setContentView(binding.root)

        val backFragment = intent.getIntExtra("backFragment", 0)
        if (backFragment == 1) {
            replaceFragment(TabFriendsFragment())
        } else {
            replaceFragment(RoomMessageFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.messenger -> replaceFragment(RoomMessageFragment())
                R.id.friends -> replaceFragment(TabFriendsFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {

                }
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        val currentId = firebaseAuth.uid
        database.reference.child("Users")
            .child(currentId!!).child("presence").setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = firebaseAuth.uid
        database.reference.child("Users")
            .child(currentId!!).child("presence").setValue("Offline")
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}