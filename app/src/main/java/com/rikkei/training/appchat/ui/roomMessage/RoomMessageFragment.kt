package com.rikkei.training.appchat.ui.roomMessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
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
            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                filter(searchQuery)
            }
        })
    }

    private fun filter(searchQuery: String) {
        val myUid = firebaseAuth.uid ?: ""
        val usersRef: DatabaseReference = database.getReference("Users")
        val roomsRef: DatabaseReference = database.getReference("Room")

        roomsRef.orderByChild("lastMessage")
            .startAt(searchQuery).endAt(searchQuery + "\uf8ff").addValueEventListener(object : ValueEventListener {
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



    private fun buttonListener() {
        binding.edSearchMess.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tvClearTextFr.isVisible = true
                binding.ivDeleteText.isVisible = true
                listRoom.clear()
            } else {
                binding.edSearchMess.hideKeyboard()
            }
        }

        binding.tvClearTextFr.setOnClickListener{
            binding.tvClearTextFr.isVisible = false
            binding.ivDeleteText.isVisible = false
            showRoomInfo()
            binding.edSearchMess.clearFocus()
        }

        binding.ivDeleteText.setOnClickListener{
            binding.edSearchMess.text?.clear()
        }

        binding.edSearchMess.setOnKeyListener(object : OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == KeyEvent.KEYCODE_DEL) {
                    listRoom.clear()
                }
                return false
            }

        })
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
