package com.rikkei.training.appchat.ui.message

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ActivityMessengerBinding
import com.rikkei.training.appchat.model.MessageModel
import com.rikkei.training.appchat.model.ItemMessageRVModel
import com.rikkei.training.appchat.ui.home.HomeActivity
import com.rikkei.training.appchat.ui.tabIcon.FragmentIcon
import com.rikkei.training.appchat.model.IconModel
import com.rikkei.training.appchat.ui.tabGallery.FragmentGallery
import java.text.SimpleDateFormat
import java.util.Date


class ActivityMessage : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var iconList: ArrayList<IconModel> = arrayListOf()

    private val messageList: ArrayList<ItemMessageRVModel> = arrayListOf()

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        binding.frameLayoutMess.visibility = View.GONE
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount == 0) {
                // Back stack is empty, hide the FrameLayout or handle the behavior you desire
                binding.frameLayoutMess.visibility = View.GONE
            } else {
                // Back stack is not empty, show the FrameLayout
                binding.frameLayoutMess.visibility = View.VISIBLE
            }
        }
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        database.reference.child("Users")
            .child(firebaseAuth.uid ?: "").child("presence").setValue("Online")
        addIcon()
        infoUserChat()
        backHome()
    }

    private fun addIcon() {
        iconList.add(IconModel(R.drawable.dumbbell, "dumbbell"))
        iconList.add(
            IconModel(
                R.drawable.great_job_good_job_sticker_collection,
                "great_job_good_job_sticker_collection"
            )
        )
        iconList.add(IconModel(R.drawable.online_training, "online_training"))
        iconList.add(IconModel(R.drawable.play_with_pet, "play_with_pet"))
        iconList.add(IconModel(R.drawable.reading, "reading"))
        iconList.add(IconModel(R.drawable.video_calling, "video_calling"))
        iconList.add(IconModel(R.drawable.watering_plants, "watering_plants"))

    }

    private fun backHome() {
        binding.imBackHome.setOnClickListener {
            val homeIntent = Intent(this, HomeActivity::class.java)
            val backFragment = 1
            homeIntent.putExtra("backFragment", backFragment)
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
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvPresence.text = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun createRoom(myUid: String, uidFriend: String, roomId: String) {
        database.reference.child("Room").child(roomId).child("member").child(myUid)
            .setValue("member")
            .addOnSuccessListener {
                database.reference.child("Room").child(roomId).child("member").child(uidFriend)
                    .setValue("member")
            }
    }

    private fun updateRoomInfo(roomId: String, timeStamp: Long, imgProfile: String?) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["lastMessage"] = binding.etSend.text.toString()
        hashMap["unreadMessage"] = 1
        database.reference.child("Room").child(roomId).updateChildren(hashMap)

    }

    private fun sendMessage(roomId: String, timeStamp: Long) {
        binding.ivSend.setOnClickListener {
            fun convertLongToTime(timeNow: Long): String {
                val date = Date(timeNow)
                val format = SimpleDateFormat("dd.MM HH:mm")
                return format.format(date)
            }

            val content = binding.etSend.text.toString()
            val mess =
                MessageModel(null, content, firebaseAuth.uid, convertLongToTime(timeStamp), null)
            database.reference.child("Message").child(roomId).push().setValue(mess)
            binding.etSend.text.clear()
        }
    }

    private fun sendImageIcon(roomId: String, iconList: ArrayList<IconModel>) {
        binding.ivLibrary.setOnClickListener {
            fragmentPhoto(roomId)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.ivLibrary.windowToken, 0)
            if (binding.frameLayoutMess.visibility == View.VISIBLE) {
                binding.frameLayoutMess.visibility = View.GONE
            } else {
                binding.frameLayoutMess.visibility = View.VISIBLE
            }
        }

        binding.ivIcon.setOnClickListener {
            fragmentIcon(roomId, iconList)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.ivIcon.windowToken, 0)
            if (binding.frameLayoutMess.visibility == View.VISIBLE) {
                binding.frameLayoutMess.visibility = View.GONE
            } else {
                binding.frameLayoutMess.visibility = View.VISIBLE
            }
        }
    }

    private fun getAllMess(roomId: String, imgProfile: String?) {
        database.reference.child("Message").child(roomId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Clear messageList only when loading messages for the first time
                    val isFirstLoad = messageList.isEmpty()
                    if (isFirstLoad) {
                        messageList.clear()
                    }

                    for (snap in snapshot.children) {
                        val content = snap.child("content").getValue(String::class.java)
                        val senderId = snap.child("senderId").getValue(String::class.java)
                        val myUid = firebaseAuth.uid ?: ""
                        val iconName = snap.child("iconName").getValue(String::class.java)
                        val mess = snap.getValue(MessageModel::class.java)
                        mess?.imgIcon = iconName

                        if (senderId == myUid) {
                            if (content != null) {
                                mess?.let { messageList.add(ItemMessageRVModel(it, true, 1, "")) }
                            } else {
                                if (iconName != null) {
                                    mess?.let {
                                        messageList.add(
                                            ItemMessageRVModel(
                                                it,
                                                true,
                                                3,
                                                ""
                                            )
                                        )
                                    }
                                } else {
                                    mess?.let {
                                        messageList.add(
                                            ItemMessageRVModel(
                                                it,
                                                true,
                                                2,
                                                ""
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            mess?.imgFriend = imgProfile
                            if (content != null) {
                                mess?.let { messageList.add(ItemMessageRVModel(it, false, 1, "")) }
                            } else {
                                if (iconName != null) {
                                    mess?.let {
                                        messageList.add(
                                            ItemMessageRVModel(
                                                it,
                                                false,
                                                3,
                                                ""
                                            )
                                        )
                                    }
                                } else {
                                    mess?.let {
                                        messageList.add(
                                            ItemMessageRVModel(
                                                it,
                                                false,
                                                2,
                                                ""
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Notify the adapter only when loading messages for the first time
                    if (isFirstLoad) {
                        messageAdapter = MessageAdapter(messageList)
                        binding.rvMesHomeMes.adapter = messageAdapter
                    }
                    messageAdapter.notifyDataSetChanged()
                    binding.rvMesHomeMes.smoothScrollToPosition(messageAdapter.itemCount)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fragmentPhoto(roomId: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentGallery = FragmentGallery()
        val photoBundle = Bundle()
        photoBundle.putString("roomId", roomId)
        fragmentGallery.arguments = photoBundle
        fragmentTransaction.replace(R.id.frame_layout_mess, fragmentGallery)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        fragmentManager.isDestroyed
    }

    private fun fragmentIcon(roomId: String, iconList: ArrayList<IconModel>) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentIcon = FragmentIcon()
        val iconBundle = Bundle()
        iconBundle.putString("roomId", roomId)
        iconBundle.putParcelableArrayList("iconList", iconList)
        fragmentIcon.arguments = iconBundle
        fragmentTransaction.replace(R.id.frame_layout_mess, fragmentIcon)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        fragmentManager.isDestroyed
    }
    override fun onPause() {
        super.onPause()
        database.reference.child("Users")
            .child(firebaseAuth.uid ?: "").child("presence").setValue("Offline")
    }

    private fun infoUserChat() {
        val name = intent.getStringExtra("name")
        val imgProfile = intent.getStringExtra("img")
        val uidUser = intent.getStringExtra("uid")
        val uidFriend = uidUser.toString()
        val myUid = firebaseAuth.uid ?: ""
        val timeStamp = System.currentTimeMillis()
        val roomId = if (myUid > uidFriend) {
            "$myUid$uidFriend"
        } else {
            "$uidFriend$myUid"
        }

        messageAdapter = MessageAdapter(messageList)
        binding.rvMesHomeMes.adapter = messageAdapter
        binding.rvMesHomeMes.setHasFixedSize(true)


        infoUserProfile(name, imgProfile, uidUser)
        createRoom(myUid, uidFriend, roomId)
        getAllMess(roomId, imgProfile)
        sendMessage(roomId, timeStamp)
        sendImageIcon(roomId, iconList)
        updateRoomInfo(roomId, timeStamp, imgProfile)
    }

    private val keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        try {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)

            val height = window.decorView.height
            if (height - r.bottom > height * 0.1399) {
                binding.frameLayoutMess.visibility = View.GONE
            } else {
                //keyboard is close
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onStart() {
        super.onStart()

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
    }

    override fun onStop() {
        super.onStop()

        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
    }
}