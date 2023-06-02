package com.rikkei.training.appchat.ui.roomMessage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.databinding.FragmentRoomMessengerBinding
import com.rikkei.training.appchat.model.RoomModel
import com.rikkei.training.appchat.ui.message.MessageActivity
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
    }

    private fun showRoomInfo() {
        roomAdapter = RoomMessengerAdapter(listRoom, object : RoomItemClick {
            override fun getRoomInfo(roomModel: RoomModel) {
                val messIntent = Intent(activity, MessageActivity::class.java)
                Intent.FLAG_ACTIVITY_SINGLE_TOP
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
                    val roomId = snapshot.key
                    val uidFriend = roomId?.substringAfter(myUid)
                    val room = snapshot.getValue(RoomModel::class.java)
                    if (roomId != null && roomId.contains(myUid)) {
                        room?.lastMessage = snapshot.child("lastMessage").value.toString()
                        room?.timeStamp = snapshot.child("timeStamp").value.toString()

                        usersRef.child(uidFriend.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                room?.imgRoom = userSnapshot.child("img").value.toString()

                                room?.let { listRoom.add(it) }

                                roomAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("RoomMessageFragment", "Error: ${error.message}")
                            }
                        })
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("RoomMessageFragment", "Error: ${databaseError.message}")
            }
        })
        binding.rvMesHomeMes.adapter = roomAdapter
    }
}
