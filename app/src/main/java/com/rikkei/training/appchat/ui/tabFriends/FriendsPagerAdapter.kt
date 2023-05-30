package com.rikkei.training.appchat.ui.tabFriends

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.ui.tabRequest.TabRequestFragment
import com.rikkei.training.appchat.ui.tabUser.TabUserFragment


class FriendsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> FriendsFragment()
            1 -> TabUserFragment()
            2 -> TabRequestFragment()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")

        }
    }

}
