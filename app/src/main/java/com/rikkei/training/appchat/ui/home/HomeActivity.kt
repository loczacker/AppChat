package com.rikkei.training.appchat.ui.home

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.ui.tabFriends.HomeFriendsFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        badgeSetup(R.id.nav_messenger, 7)
        badgeSetup(R.id.nav_friends, 10)


        val backFragment = intent.getIntExtra("backFragment", 0)
        if (backFragment == 1) {
            replaceFragment(HomeFriendsFragment())
        } else {
            replaceFragment(RoomMessageFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_messenger -> {
                    replaceFragment(RoomMessageFragment())
                    badgeClear(R.id.nav_messenger)
                }
                R.id.nav_friends -> {
                    replaceFragment(HomeFriendsFragment())
                    badgeClear(R.id.nav_friends)
                }
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }

    }

    private fun badgeSetup(id: Int, alerts: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(id)
        badge.isVisible = true
        badge.number = alerts
    }

    private fun badgeClear(id: Int) {
        val badgeDrawable = binding.bottomNavigationView.getBadge(id)
        if (badgeDrawable != null) {

            badgeDrawable.isVisible = false

            badgeDrawable.clearNumber()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentId = firebaseAuth.uid
        currentId?.let { userId ->
            database.reference.child("Users")
                .child(userId)
                .child("presence")
                .setValue("Online")
        }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
    }

    override fun onStop() {
        val currentId = firebaseAuth.uid
        currentId?.let { userId ->
            database.reference.child("Users")
                .child(userId)
                .child("presence")
                .setValue("Offline")
        }
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
        super.onStop()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
