package com.rikkei.training.appchat.ui.roomMessage

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
        roomAdapter = RoomMessengerAdapter(listRoom, object : RoomItem{
            override fun getRoomInfo(roomModel: RoomModel) {
                val messIntent = Intent(activity, MessageActivity::class.java)
                Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(messIntent)
            }
        })
        binding.rvMesHomeMes.adapter = roomAdapter
        database.reference.child("Room").orderByChild("member").equalTo(firebaseAuth.uid?:"")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listRoom.clear()
                    for (snapshotRoom in snapshot.children) {
                        val room = snapshotRoom.getValue(RoomModel::class.java)
                        room!!.imgRoom = snapshotRoom.child("").value.toString()
                        room.lastMessage = snapshotRoom.child("lastMessage").value.toString()
                        room.timeStamp = snapshotRoom.child("timeStamp").value.toString()
                        listRoom.add(room)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        roomAdapter.notifyDataSetChanged()
    }
}