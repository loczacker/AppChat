package com.rikkei.training.appchat.ui.tabRequest

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

    private lateinit var requestAdapter: RequestFriendsSentAdapter


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
        requestAdapter = RequestFriendsSentAdapter(usersRequest, object : ItemUsersRecycleView {
            override fun getDetail(user: UsersModel) {

            }
        })

        binding.rvSendRequestFriend.adapter = requestAdapter
       database.reference.child("friendsRequest").child(firebaseAuth.uid?:"")
           .addValueEventListener(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   usersRequest.clear()
                   for (postSnapshot in snapshot.children) {
                   val user = postSnapshot.getValue(UsersModel::class.java)
                       database.reference.child("Users").child(user?.uid.toString())
                           .addListenerForSingleValueEvent(object : ValueEventListener{
                               override fun onDataChange(snapshot: DataSnapshot) {
                                   if (snapshot.exists()) {
                                       val name = snapshot.child("name").value.toString()
                                       val img  = snapshot.child("img").value.toString()

                                       val user = UsersModel(null,null,img,name,null,null,null)
                                       usersRequest.add(user)

                                   }
                               }
                               override fun onCancelled(error: DatabaseError) {}
                           })
                    user?.let { usersRequest.add(it) }
                }
                requestAdapter.notifyDataSetChanged()
               }

               override fun onCancelled(error: DatabaseError) {}

           })

    }


}
