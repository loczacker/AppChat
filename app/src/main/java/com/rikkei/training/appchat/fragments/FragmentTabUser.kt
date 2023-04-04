package com.rikkei.training.appchat.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.Users
import com.rikkei.training.appchat.adapter.MessengerAdapter
import com.rikkei.training.appchat.databinding.FragmentMessengerBinding
import com.rikkei.training.appchat.databinding.FragmentTabFriendsBinding
import com.rikkei.training.appchat.databinding.FragmentTabUserBinding

class FragmentTabUser : Fragment() {

    private lateinit var binding: FragmentTabUserBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var users: ArrayList<Users>
    private lateinit var messengerAdapter: MessengerAdapter
    private lateinit var dialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabUserBinding.inflate(inflater, container, false)
        return binding.root
    }
}