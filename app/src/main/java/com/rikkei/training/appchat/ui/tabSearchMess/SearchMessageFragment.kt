package com.rikkei.training.appchat.ui.tabSearchMess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentSearchMessageBinding


class SearchMessageFragment : Fragment() {

    private lateinit var binding: FragmentSearchMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMessageBinding.inflate(inflater, container, false)
        return binding.root
    }
}
