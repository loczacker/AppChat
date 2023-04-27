package com.rikkei.training.appchat.ui.tabUser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.databinding.FragmentTabUserBinding
import com.rikkei.training.appchat.model.UsersModel
import java.util.ArrayList

class FragmentTabUser : Fragment() {

    private lateinit var binding: FragmentTabUserBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val listUser: ArrayList<ItemRecyclerViewModel> = arrayListOf()
    private val listFriend = hashMapOf<String, String>()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun sendRequest(uid: String) {
        database.reference.child("Friends").child(uid)
            .child(firebaseAuth.uid ?: "").child("status").setValue("received")
            .addOnSuccessListener {
                database.reference.child("Friends").child(firebaseAuth.uid ?: "")
                    .child(uid).child("status").setValue("sent")
                    .addOnSuccessListener {
                    }
            }
            .addOnFailureListener {
            }
    }

    private fun getAllUser() {
        database.reference.child("Users")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user == null || user.uid == firebaseAuth.uid) {
                        return
                    }
                    updateOrInsertUser(user)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user == null || user.uid == firebaseAuth.uid) {
                        return
                    }
                    updateOrInsertUser(user)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user == null || user.uid == firebaseAuth.uid) {
                        return
                    }
                    listUser.removeIf {
                        it.user.uid == user.uid
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    fun updateOrInsertUser(user: UsersModel) {
        var isHaveItem = false
        listUser.forEachIndexed { index, item ->
            if (item.user.uid == user.uid) {
                item.user = user
                usersAdapter.notifyItemChanged(index)
                isHaveItem = true
            }
        }
        if (isHaveItem) return
        listUser.add(ItemRecyclerViewModel(user))
        usersAdapter.notifyItemInserted(listUser.size)
    }

    private fun getAllFriendRelation() {
        database.reference.child("Friends").child(firebaseAuth.uid ?: "")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val friendStatus = snapshot.child("status").getValue(String::class.java) ?: ""
                    val friendId = snapshot.key ?: ""
                    listFriend[friendId] = friendStatus
                    listUser.forEachIndexed { index, item ->
                        if (item.user.uid == friendId){
                            item.statusButton = friendStatus
                            usersAdapter.notifyItemChanged(index)
                        }
                    }
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val friendStatus = snapshot.child("status").getValue(String::class.java) ?: ""
                    val friendId = snapshot.key ?: ""
                    listFriend[friendId] = friendStatus

                    listUser.forEachIndexed { index, item ->
                        if (item.user.uid == friendId){
                            item.statusButton = friendStatus
                            usersAdapter.notifyItemChanged(index)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun disPlayInfoUsers() {
        usersAdapter = UserAllAdapter(listUser, object : ItemUsersRecycleView {
            override fun getDetail(item: ItemRecyclerViewModel) {
                sendRequest(item.user.uid.toString())
            }
        })
        binding.recyclerViewTabUser.adapter = usersAdapter
        listUser.clear()
        getAllUser()
        getAllFriendRelation()
    }
}