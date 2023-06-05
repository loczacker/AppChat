package com.rikkei.training.appchat.ui.tabNotFound

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentNotFoundBinding

class NotFoundFragment : Fragment() {

    private lateinit var binding: FragmentNotFoundBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotFoundBinding.inflate(inflater, container, false)
        return binding.root
    }
}