package com.rikkei.training.appchat.ui.roomMessage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentRoomMessengerBinding
import com.rikkei.training.appchat.model.RoomModel
import com.rikkei.training.appchat.ui.message.MessageActivity
import com.rikkei.training.appchat.ui.tabSearchMess.SearchMessageFragment
import java.util.ArrayList

class RoomMessageFragment : Fragment() {

    private lateinit var binding: FragmentRoomMessengerBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val listRoom: ArrayList<RoomModel> = arrayListOf()

    private lateinit var roomAdapter: RoomMessengerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomMessengerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRoomInfo()
        searchMessage()
    }

    private fun searchMessage() {


        binding.svSearchMess.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                val searchMessageFragment = SearchMessageFragment()
                fragmentTransaction.replace(R.id.frameLayoutRoomMess, searchMessageFragment)
                fragmentTransaction.commit()
            }
        }

        binding.svSearchMess.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showRoomInfo() {
        roomAdapter = RoomMessengerAdapter(listRoom, object : RoomItemClick {
            override fun getRoomInfo(roomModel: RoomModel) {
                val messIntent = Intent(activity, MessageActivity::class.java)
                messIntent.putExtra("name", roomModel.nameRoom)
                messIntent.putExtra("img", roomModel.imgRoom)
                messIntent.putExtra("uid", roomModel.uidFriend)
                startActivity(messIntent)
            }
        })

        val myUid = firebaseAuth.uid ?: ""

        val usersRef: DatabaseReference = database.getReference("Users")
        val roomsRef: DatabaseReference = database.getReference("Room")

        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listRoom.clear()
                for (snapshot in dataSnapshot.children) {
                    val roomId = snapshot.key.toString()
                    val room = snapshot.getValue(RoomModel::class.java)
                    room?.uidFriend = extractUidFriend(roomId, myUid)
                    if (roomId.contains(myUid)) {
                        room?.lastMessage = snapshot.child("lastMessage").value.toString()
                        room?.timeStamp = snapshot.child("timeStamp").value.toString()
                        usersRef.child(room?.uidFriend!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(userSnapshot: DataSnapshot) {
                                    room?.imgRoom = userSnapshot.child("img").value.toString()
                                    room?.nameRoom = userSnapshot.child("name").value.toString()
                                    roomAdapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e("RoomMessageFragment", "Error: ${error.message}")
                                }
                            })
                    }
                    room?.let { listRoom.add(it) }
                }
                roomAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("RoomMessageFragment", "Error: ${databaseError.message}")
            }
        })

        binding.rvMesHomeMes.adapter = roomAdapter
    }

    private fun extractUidFriend(roomId: String, myUid: String): String {
        return if (roomId.startsWith(myUid)) {
            roomId.removePrefix(myUid)
        } else {
            roomId.substringBefore(myUid)
        }

    }
}
