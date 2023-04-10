package com.rikkei.training.appchat.ui.tabUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.databinding.FragmentTabUserBinding
import com.rikkei.training.appchat.ui.tabRequest.FragmentTabRequest

class FragmentTabUser : Fragment(){

    private lateinit var binding: FragmentTabUserBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private lateinit var requestRef : DatabaseReference

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val users: ArrayList<UsersModel> = arrayListOf()

    private lateinit var usersAdapter: UserAllAdapter

    private var currentState: String =  "nothing_happen"



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabUserBinding.inflate(inflater, container, false)
        disPlayInfoUsers()
        return binding.root
    }

    private fun disPlayInfoUsers() {
        usersAdapter = UserAllAdapter(users,object: ItemUsersRecycleView{
            override fun getDetail(position: Int) {
            }
        })
        binding.recyclerViewTabUser.adapter = usersAdapter

        users.clear()
        database.reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val user = postSnapshot.getValue(UsersModel::class.java)
                     if (user?.uid != firebaseAuth.uid) {
                         user?.let { users.add(it) }

                     }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("presence")
            .child(currentId!!).setValue("Offline")
    }



}