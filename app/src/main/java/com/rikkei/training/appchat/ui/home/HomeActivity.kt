package com.rikkei.training.appchat.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.ui.tabFriends.FragmentFriendsTab
import com.rikkei.training.appchat.ui.Messenger.FragmentMessenger
import com.rikkei.training.appchat.ui.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(FragmentMessenger())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.messenger -> replaceFragment(FragmentMessenger())
                R.id.friends -> replaceFragment(FragmentFriendsTab())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {

                }
            }

            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}