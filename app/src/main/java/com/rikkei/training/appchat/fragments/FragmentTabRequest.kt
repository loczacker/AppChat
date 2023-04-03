package com.rikkei.training.appchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentMessengerBinding
import com.rikkei.training.appchat.databinding.FragmentTabFriendsBinding
import com.rikkei.training.appchat.databinding.FragmentTabRequestBinding

class FragmentTabRequest : Fragment() {

    private lateinit var binding: FragmentTabRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabRequestBinding.inflate(inflater, container, false)
        return binding.root
    }
}