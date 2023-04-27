package com.rikkei.training.appchat.ui.Messenger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.databinding.FragmentMessengerBinding

class FragmentMessenger : Fragment() {

    private lateinit var binding: FragmentMessengerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessengerBinding.inflate(inflater, container, false)
        return binding.root
    }
}