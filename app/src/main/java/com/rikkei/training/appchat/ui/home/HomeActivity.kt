package com.rikkei.training.appchat.ui.home

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.ui.tabFriends.TabFriendsFragment
import com.rikkei.training.appchat.ui.roomMessage.RoomMessageFragment
import com.rikkei.training.appchat.ui.profile.ProfileFragment
import com.rikkei.training.appchat.ui.tabSearchMess.SearchMessageFragment

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

    private val keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        try {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)

            val height = window.decorView.height
            binding.bottomNavigationView.isVisible = height - r.bottom <= height * 0.1399
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
    }

    override fun onStop() {
        super.onStop()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
    }

}