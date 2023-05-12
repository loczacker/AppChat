package com.rikkei.training.appchat.ui.message

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityMessengerBinding
import com.rikkei.training.appchat.model.MessageModel
import com.rikkei.training.appchat.model.MessageRecyclerViewModel
import com.rikkei.training.appchat.ui.home.HomeActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Calendar


class ActivityMessage : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val messageList: ArrayList<MessageRecyclerViewModel> = arrayListOf()
    private val messMap = hashMapOf<String, Int>()
    private lateinit var messageAdapter: MessageAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        database.reference.child("Users")
            .child(firebaseAuth.uid?:"").child("presence").setValue("Online")
        infoUserChat()
        backHome()
    }

    private fun backHome() {
        binding.imBackHome.setOnClickListener{
            val homeIntent = Intent(this, HomeActivity::class.java)
            homeIntent.putExtra("currentFragment", "FragmentFriends")
            startActivity(homeIntent)
            finish()
        }
    }

    private fun infoUserProfile(name: String?, imgProfile: String?, uidUser: String?) {
        binding.tvNameMess.text = name
        Glide.with(this@ActivityMessage)
            .load(imgProfile)
            .transform(CenterCrop(), RoundedCorners(55))
            .placeholder(R.drawable.profile)
            .into(binding.ivImgProfile)
        database.reference.child("Users").child(uidUser.toString()).child("presence")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvPresence.text = snapshot.value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun createRoom(myUid: String, uidFriend: String, roomId: String) {
        database.reference.child("Room").child(roomId).child("member").child(myUid).setValue("member")
            .addOnSuccessListener {
                database.reference.child("Room").child(roomId).child("member").child(uidFriend).setValue("member")
            }
    }

//    private fun updateRoomInfo(roomId: String, content: String) {
//        val hashMap: HashMap<String, Any> = HashMap()
//        hashMap["lastMessage"] = binding.etSend.text.toString()
//        hashMap["unreadMessage"] = 1
//        database.reference.child("Room").child(roomId).updateChildren(hashMap)
//
//    }

    private fun sendMessage(roomId: String) {
        binding.ivSend.setOnClickListener{
            val timeNow = Calendar.getInstance().time
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM")
            val content = binding.etSend.text.toString()
            val mess = MessageModel(content, firebaseAuth.uid, timeNow.toString(), null )
            database.reference.child("Message").child(roomId).push().setValue(mess)
            binding.etSend.text.clear()
        }
    }

    private fun getAllMess(roomId: String) {
        database.reference.child("Message").child(roomId)
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val senderId = snap.child("senderId").getValue(String::class.java)
                        val myUid = firebaseAuth.uid?:""
                        val mess = snap.getValue(MessageModel::class.java)
                        if (senderId == myUid) {
                            mess?.let { messageList.add(MessageRecyclerViewModel(it, 0, "")) }
                        } else if (senderId != myUid) {
                            mess?.let { messageList.add(MessageRecyclerViewModel(it, 1, "" )) }
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    override fun onPause() {
        super.onPause()
        database.reference.child("Users")
            .child(firebaseAuth.uid?:"").child("presence").setValue("Offline")
    }

    private fun infoUserChat() {

        messageAdapter = MessageAdapter(messageList)
        binding.rvMesHomeMes.adapter = messageAdapter
        messageList.clear()

        val name = intent.getStringExtra("name")
        val imgProfile = intent.getStringExtra("img")
        val uidUser = intent.getStringExtra("uid")
        val uidFriend = uidUser.toString()
        val myUid = firebaseAuth.uid ?: ""

        val roomId = if (myUid > uidFriend) {
            "$myUid$uidFriend"
        } else {
            "$uidFriend$myUid"
        }
        getAllMess(roomId)
        infoUserProfile(name, imgProfile, uidUser)
        createRoom(myUid,uidFriend, roomId)
//        updateRoomInfo(roomId, content)
        sendMessage(roomId)
    }

}