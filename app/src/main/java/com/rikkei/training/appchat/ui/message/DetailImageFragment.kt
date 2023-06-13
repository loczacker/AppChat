package com.rikkei.training.appchat.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.databinding.FragmentDetailImageBinding

class DetailImageFragment : Fragment() {

    private lateinit var binding : FragmentDetailImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailImageBinding.inflate(inflater, container, false)
        return binding.root
    }

}