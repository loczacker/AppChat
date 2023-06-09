package com.rikkei.training.appchat.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.ui.tabFriends.TabFriendsFragment
import com.rikkei.training.appchat.ui.profile.ProfileFragment
import com.rikkei.training.appchat.ui.roomMessage.RoomMessageFragment

class HomePagerAdapter(activity: HomeActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RoomMessageFragment()
            1 -> TabFriendsFragment()
            2 -> ProfileFragment()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")
        }
    }
}