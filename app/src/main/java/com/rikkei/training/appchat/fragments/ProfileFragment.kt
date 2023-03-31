package com.rikkei.training.appchat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.ibChange.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, ChangeProfileFragment())?.commit()
        }
        return binding.root
    }


    private fun readData() {
        if (firebaseAuth.currentUser?.uid == null){
            return
        }
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(firebaseAuth.currentUser?.uid!!).get().addOnSuccessListener {
            val email = it.child("email").value
            val name = it.child("name").value
            binding.tvEmailProfile.text = email.toString()
            binding.tvNameProfile.text = name.toString()
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

}