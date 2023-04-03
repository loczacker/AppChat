package com.rikkei.training.appchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentMessengerBinding
import com.rikkei.training.appchat.databinding.FragmentTabFriendsBinding

class FragmentTabFriends : Fragment() {

    private lateinit var binding: FragmentTabFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }
}