package com.rikkei.training.appchat.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.ui.tabFriends.FragmentFriendsTab
import com.rikkei.training.appchat.ui.roomMessage.FragmentRoomMessage
import com.rikkei.training.appchat.ui.profile.FragmentProfile

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
        setContentView(binding.root)

        val backFragment = intent.getIntExtra("backFragment", 0)
        if (backFragment == 1) {
            replaceFragment(FragmentFriendsTab())
        } else {
            replaceFragment(FragmentRoomMessage())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.messenger -> replaceFragment(FragmentRoomMessage())
                R.id.friends -> replaceFragment(FragmentFriendsTab())
                R.id.profile -> replaceFragment(FragmentProfile())

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