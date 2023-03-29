package com.rikkei.training.appchat.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.activity.HomeActivity
import com.rikkei.training.appchat.fragments.FriendsFragment
import com.rikkei.training.appchat.fragments.MessengerFragment
import com.rikkei.training.appchat.fragments.ProfileFragment

class HomePagerAdapter(activity: HomeActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MessengerFragment()
            1 -> FriendsFragment()
            2 -> ProfileFragment()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")
        }
    }

}