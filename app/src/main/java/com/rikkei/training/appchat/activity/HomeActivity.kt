package com.rikkei.training.appchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.adapter.HomePagerAdapter
import com.rikkei.training.appchat.databinding.ActivityHomeBinding
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding
import com.rikkei.training.appchat.fragments.ChangeProfileFragment
import com.rikkei.training.appchat.fragments.FriendsFragment
import com.rikkei.training.appchat.fragments.MessengerFragment
import com.rikkei.training.appchat.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MessengerFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.messenger -> replaceFragment(MessengerFragment())
                R.id.friends -> replaceFragment(FriendsFragment())
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

