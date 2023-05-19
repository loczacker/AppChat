package com.rikkei.training.appchat.ui.tabPhoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.rikkei.training.appchat.databinding.FragmentIconBinding
import com.rikkei.training.appchat.databinding.FragmentPhotoLibraryBinding

class FragmentGallery : Fragment() {

    private lateinit var binding: FragmentPhotoLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}