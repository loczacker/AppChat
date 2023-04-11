package com.rikkei.training.appchat.ui.tabRequest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.databinding.FragmentTabRequestBinding
import com.rikkei.training.appchat.model.UsersModel

class FragmentTabRequest : Fragment() {

    private lateinit var binding: FragmentTabRequestBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val usersRequest: ArrayList<UsersModel> = arrayListOf()

    private lateinit var requestAdapter: RequestFriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRequestUser()
    }

    private fun showRequestUser() {
        requestAdapter = RequestFriendsAdapter(usersRequest, object : ItemUserRequest{
            override fun getRequest(user: UsersModel) {

            }
        })

        binding.rvSendRequestFriend.adapter = requestAdapter
        usersRequest.clear()
        database.reference.child("Request").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val user = postSnapshot.getValue(UsersModel::class.java)
                    user?.let { usersRequest.add(it) }
                }
                requestAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
