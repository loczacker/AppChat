package com.rikkei.training.appchat.ui.roomMessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.databinding.FragmentRoomMessengerBinding
import com.rikkei.training.appchat.model.RoomModel
import com.rikkei.training.appchat.ui.message.MessageActivity
import kotlin.collections.ArrayList

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
        buttonListener()
        binding.edSearchMess.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                roomAdapter.clearList()
            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                if (searchQuery.isNotEmpty()) {
                    filter(searchQuery)
                }
            }
        })
    }

    private fun filter(searchQuery: String) {
        val myUid = firebaseAuth.uid ?: ""
        val roomsMess = database.getReference("Message")
        roomsMess.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listRoom.clear()
                val tempListRoom: ArrayList<RoomModel> = ArrayList()
                for (snapshot in dataSnapshot.children) {
                    val roomId = snapshot.key.toString()
                    val room = snapshot.getValue(RoomModel::class.java)
                    room?.uidFriend = extractUidFriend(roomId, myUid)
                    if (roomId.contains(myUid)) {
                        checkMess(roomId, searchQuery)
                    }
                }
                if (tempListRoom.isEmpty()) {
                    binding.layoutNotFound.visibility = View.VISIBLE
                } else {
                    binding.layoutNotFound.visibility = View.GONE
                }
                listRoom.addAll(tempListRoom)
                roomAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }



    private fun checkMess(roomId: String, searchQuery: String) {
        val myUid = firebaseAuth.uid.toString()
        val usersRef: DatabaseReference = database.getReference("Users")
        database.reference.child("Message").child(roomId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uniqueMessages: ArrayList<String> = ArrayList()
                    val tempListRoom: ArrayList<RoomModel> = ArrayList()
                    for (messSnapshot in snapshot.children) {
                        val senderId = messSnapshot.child("senderId").value.toString()
                        val content = messSnapshot.child("content").value.toString()
                        if (content.contains(searchQuery) || content == searchQuery) {
                            if (senderId != firebaseAuth.uid) {
                                val message = "$senderId-$content"
                                if (!uniqueMessages.contains(message)) {
                                    uniqueMessages.add(message)
                                    val newRoom = RoomModel().apply {
                                        this.contentMess = content
                                    }
                                    tempListRoom.add(newRoom)
                                }
                            }
                        }
                    }
                    tempListRoom.forEach { roomModel ->
                        val senderId = extractUidFriend(roomId, myUid)
                        usersRef.child(senderId).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                roomModel.imgRoom = userSnapshot.child("img").value.toString()
                                roomModel.nameRoom = userSnapshot.child("name").value.toString()
                                roomAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                    listRoom.addAll(tempListRoom)
                    roomAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    private fun buttonListener() {
        binding.edSearchMess.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tvClearTextFr.isVisible = true
                binding.ivDeleteText.isVisible = true
                roomAdapter.clearList()
            } else {
                binding.edSearchMess.hideKeyboard()
            }
        }

        binding.tvClearTextFr.setOnClickListener {
            binding.tvClearTextFr.isVisible = false
            binding.ivDeleteText.isVisible = false
            binding.edSearchMess.text?.clear()
            showRoomInfo()
            binding.edSearchMess.clearFocus()
        }

        binding.ivDeleteText.setOnClickListener {
            binding.edSearchMess.text?.clear()
            binding.edSearchMess.hideKeyboard()
            roomAdapter.clearList()
        }

        binding.edSearchMess.setOnKeyListener { _, p1, _ ->
            if (p1 == KeyEvent.KEYCODE_DEL) {
                roomAdapter.clearList()
            }
            false
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showRoomInfo() {
        roomAdapter = RoomMessengerAdapter(listRoom, object : ItemClick {
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
                        val lastMessage = snapshot.child("lastMessage").value.toString()
                        val senderId = snapshot.child("senderId").value.toString()
                        room?.typeRoomMess = true
                        room?.timeStamp = snapshot.child("timeStamp").value.toString()
                        val unreadMess = snapshot.child("member").child(firebaseAuth.uid ?: "").child("unread messages").value.toString()
                        if (unreadMess.toIntOrNull() != null && unreadMess.toInt() != 0) {
                            room?.unReadMessage = unreadMess
                            room?.isNewMessage = "not_seen"
                        } else {
                            room?.isNewMessage = "seen"
                        }
                        lastMessageCheck(lastMessage, senderId, room, snapshot)
                        usersRef.child(room?.uidFriend ?: "").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                room?.imgRoom = userSnapshot.child("img").value.toString()
                                room?.nameRoom = userSnapshot.child("name").value.toString()
                                roomAdapter.notifyDataSetChanged()
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                    room?.let { listRoom.add(it) }
                }
                roomAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        binding.rvMesHomeMes.adapter = roomAdapter
    }


    private fun lastMessageCheck(
        lastMessage: String,
        senderId: String,
        room: RoomModel?,
        snapshot: DataSnapshot?
    ) {
        if (senderId == (firebaseAuth.uid ?: "")) {
            when (lastMessage) {
                "Sticker" -> {
                    room?.lastMessage = "Bạn đã gửi một Sticker"
                }

                "Image" -> {
                    room?.lastMessage = "Bạn đã gửi một hình ảnh"
                }

                else -> {
                    room?.lastMessage = snapshot?.child("lastMessage")?.value.toString()
                }
            }
        } else {
            when (lastMessage) {
                "Sticker" -> {
                    room?.lastMessage = "Bạn của bạn đã gửi một Sticker"
                }

                "Image" -> {
                    room?.lastMessage = "Bạn của bạn đã gửi một hình ảnh"
                }

                else -> {
                    room?.lastMessage = snapshot?.child("lastMessage")?.value.toString()
                }
            }
        }
    }
    private fun extractUidFriend(roomId: String, myUid: String): String {
        return if (roomId.startsWith(myUid)) {
            roomId.removePrefix(myUid)
        } else {
            roomId.substringBefore(myUid)
        }
    }
}