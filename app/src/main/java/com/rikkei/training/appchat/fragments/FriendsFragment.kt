package com.rikkei.training.appchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)

        val tabTitles = arrayOf("Bạn bè", "Tất cả", "Yêu cầu")
        TabLayoutMediator(binding.tabFriends, binding.vpFriends) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        return binding.root
    }

}