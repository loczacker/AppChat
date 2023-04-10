package com.rikkei.training.appchat.ui.tabFriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding

class FragmentFriendsTab : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FriendsPagerAdapter(childFragmentManager, lifecycle)
        binding.vpFriends.adapter = adapter

        TabLayoutMediator(binding.tabFriends, binding.vpFriends){tab, position ->
            when(position) {
                0 -> tab.text = "BẠN BÈ"
                1 -> tab.text = "TẤT CẢ"
                2 -> tab.text = "YÊU CẦU"
            }
        }.attach()
    }

}