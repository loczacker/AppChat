package com.rikkei.training.appchat.ui.tabFriends

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.ui.tabRequest.FragmentTabRequest
import com.rikkei.training.appchat.ui.tabUser.FragmentTabUser


class FriendsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> FragmentFriends()
            1 -> FragmentTabUser()
            2 -> FragmentTabRequest()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")

        }
    }

}
