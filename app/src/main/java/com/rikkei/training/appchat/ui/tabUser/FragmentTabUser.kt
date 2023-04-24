package com.rikkei.training.appchat.ui.tabUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.databinding.FragmentTabUserBinding

class FragmentTabUser : Fragment() {

    private lateinit var binding: FragmentTabUserBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val users: ArrayList<UsersModel> = arrayListOf()

    private lateinit var usersAdapter: UserAllAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disPlayInfoUsers()
    }

    private fun disPlayInfoUsers() {
        usersAdapter = UserAllAdapter(users, object : ItemUsersRecycleView {
            override fun getDetail(user: UsersModel) {

                database.reference.child("friendsRequest").child(user.uid.toString())
                    .child(firebaseAuth.uid ?: "").child("status").setValue("received")
                    .addOnSuccessListener {
                        database.reference.child("friendsRequest").child(firebaseAuth.uid ?: "")
                            .child(user.uid.toString()).child("status").setValue("sent")
                            .addOnCompleteListener {
                            }
                    }
                    .addOnFailureListener {}
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
}