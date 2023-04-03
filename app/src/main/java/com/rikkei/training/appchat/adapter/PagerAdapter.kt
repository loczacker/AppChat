package com.rikkei.training.appchat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.fragments.*

class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FragmentTabFriends()
            1 -> FragmentTabUser()
            2 -> FragmentTabRequest()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")
        }
    }


}