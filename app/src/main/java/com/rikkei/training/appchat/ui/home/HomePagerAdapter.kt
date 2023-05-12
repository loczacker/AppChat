package com.rikkei.training.appchat.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.appchat.ui.tabFriends.FragmentFriendsTab
import com.rikkei.training.appchat.ui.roomMessage.FragmentRoomMessage
import com.rikkei.training.appchat.ui.profile.FragmentProfile

class HomePagerAdapter(activity: HomeActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FragmentRoomMessage()
            1 -> FragmentFriendsTab()
            2 -> FragmentProfile()
            else -> throw IllegalArgumentException("Unknown Fragment for position $position")
        }
    }

}