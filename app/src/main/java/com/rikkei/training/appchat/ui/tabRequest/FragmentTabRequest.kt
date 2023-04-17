package com.rikkei.training.appchat.ui.tabRequest

import android.annotation.SuppressLint
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
import com.rikkei.training.appchat.databinding.FragmentTabRequestBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class FragmentTabRequest : Fragment() {

    private lateinit var binding: FragmentTabRequestBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val usersRequest: ArrayList<UsersModel> = arrayListOf()

    private val sendUsersRequest: ArrayList<UsersModel> = arrayListOf()


    private lateinit var requestAdapter: RequestFriendsSentAdapter

    private lateinit var sentAdapter: RequestSendFriendsAdapter

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
        showSendUser()
    }

    private fun showSendUser() {
        sentAdapter = RequestSendFriendsAdapter(sendUsersRequest, object : ItemSentUsers{
            override fun getDetail(user: UsersModel) {
                database.reference.child("friendsRequest").child(user.uid.toString())
                    .child(firebaseAuth.uid ?: "").removeValue().addOnCompleteListener {
                        database.reference.child("friendsRequest").child(firebaseAuth.uid?:"").child(user.uid.toString()).removeValue()

                    }
            }

            override fun getFriends(user: UsersModel) {

                database.reference.child("friendsRequest").child(user.uid.toString())
                    .child(firebaseAuth.uid ?: "").removeValue().addOnCompleteListener {
                        database.reference.child("friendsRequest").child(firebaseAuth.uid?:"").child(user.uid.toString()).removeValue()
                            .addOnCompleteListener {
                                database.reference.child("Friends").child(user.uid.toString()).child(firebaseAuth.uid?:"").child("status").setValue("friend")
                                    .addOnSuccessListener {
                                        database.reference.child("Friends").child(firebaseAuth.uid?:"").child(user.uid.toString()).child("name").setValue("friend")
                                            .addOnCompleteListener {
                                            }
                                    }
                                    .addOnFailureListener {}
                            }

                    }

            }
        })

        binding.rvRequestFriend.adapter = sentAdapter
        database.reference.child("friendsRequest").child(firebaseAuth.uid?:"").orderByChild("status").equalTo("received")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    sendUsersRequest.clear()
                    for (postSnapshot in snapshot.children) {
                        val user = postSnapshot.getValue(UsersModel::class.java)
                        database.reference.child("Users").child(postSnapshot.key.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        user!!.name = snapshot.child("name").value.toString()
                                        user.img  = snapshot.child("img").value.toString()
                                        user.uid = snapshot.child("uid").value.toString()
                                    }
                                    user?.let { sendUsersRequest.add(it) }
                                    sentAdapter.notifyDataSetChanged()
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                    sentAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun showRequestUser() {
        requestAdapter = RequestFriendsSentAdapter(usersRequest, object : ItemUsersRecycleView {
            override fun getDetail(user: UsersModel) {

                database.reference.child("friendsRequest").child(user.uid.toString())
                    .child(firebaseAuth.uid ?: "").removeValue().addOnCompleteListener {
                        database.reference.child("friendsRequest").child(firebaseAuth.uid?:"").child(user.uid.toString()).removeValue()

                }
            }
        })

        binding.rvSendRequestFriend.adapter = requestAdapter
       database.reference.child("friendsRequest").child(firebaseAuth.uid?:"").orderByChild("status").equalTo("sent")
           .addValueEventListener(object : ValueEventListener{
               @SuppressLint("SuspiciousIndentation")
               override fun onDataChange(snapshot: DataSnapshot) {
                   usersRequest.clear()
                   for (postSnapshot in snapshot.children) {
                   val user = postSnapshot.getValue(UsersModel::class.java)
                       database.reference.child("Users").child(postSnapshot.key.toString())
                           .addListenerForSingleValueEvent(object : ValueEventListener{
                               override fun onDataChange(snapshot: DataSnapshot) {
                                   if (snapshot.exists()) {
                                       user!!.name = snapshot.child("name").value.toString()
                                       user.img  = snapshot.child("img").value.toString()
                                       user.uid = snapshot.child("uid").value.toString()
                                   }
                                   user?.let { usersRequest.add(it) }
                                   requestAdapter.notifyDataSetChanged()
                               }
                               override fun onCancelled(error: DatabaseError) {}
                           })
                }
                requestAdapter.notifyDataSetChanged()
               }

               override fun onCancelled(error: DatabaseError) {}
           })

    }


}
