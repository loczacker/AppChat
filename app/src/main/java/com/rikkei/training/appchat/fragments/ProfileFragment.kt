package com.rikkei.training.appchat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.activity.HomeActivity
import com.rikkei.training.appchat.databinding.FragmentProfileBinding
import kotlin.math.log

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.ibChange.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, ChangeProfileFragment())?.commit()
        }
        return binding.root


    }

}