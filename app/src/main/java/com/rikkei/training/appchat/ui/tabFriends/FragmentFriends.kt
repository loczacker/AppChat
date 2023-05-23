package com.rikkei.training.appchat.ui.tabFriends

import android.content.Intent
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
import com.rikkei.training.appchat.databinding.FragmentTabFriendsBinding
import com.rikkei.training.appchat.ui.tabUser.UsersModel
import com.rikkei.training.appchat.ui.message.ActivityMessage
import com.rikkei.training.appchat.ui.tabUser.ItemRecyclerViewModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRVInterface

class FragmentFriends : Fragment() {

    private lateinit var binding: FragmentTabFriendsBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val friends: ArrayList<ItemRecyclerViewModel> = arrayListOf()

    private lateinit var friendAdapter: ShowFriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFriendsList()
    }

    private fun showFriendsList() {
        friendAdapter = ShowFriendsAdapter(friends, object: ItemUsersRVInterface{
            override fun getDetail(itemUser: ItemRecyclerViewModel) {
                goMessenger(itemUser)
            }
        })

        binding.recyclerViewTabFriend.adapter = friendAdapter
        database.reference.child("Friends").child(firebaseAuth.uid?:"").orderByChild("status").equalTo("friend")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    friends.clear()
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
                                    user?.let { friends.add(ItemRecyclerViewModel(it)) }
                                    friendAdapter.notifyDataSetChanged()
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                    friendAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
    private fun goMessenger(itemUser: ItemRecyclerViewModel) {
        val messIntent = Intent(activity, ActivityMessage::class.java)
        messIntent.putExtra("name", itemUser.user.name)
        messIntent.putExtra("img", itemUser.user.img)
        messIntent.putExtra("uid", itemUser.user.uid)
        messIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(messIntent)
    }
}

