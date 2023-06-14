package com.rikkei.training.appchat.ui.message

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
import com.rikkei.training.appchat.databinding.DetailImageBinding
import com.rikkei.training.appchat.model.MessageModel
import com.rikkei.training.appchat.model.ItemMessageRVModel
import com.rikkei.training.appchat.ui.home.HomeActivity
import com.rikkei.training.appchat.ui.tabSticker.StickerFragment
import com.rikkei.training.appchat.model.IconModel
import com.rikkei.training.appchat.ui.tabGallery.GalleryFragment
import java.text.SimpleDateFormat
import java.util.Date


class MessageActivity : AppCompatActivity() {

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

    private var roomId = ""

    private var uidFriend = ""

    private lateinit var imageDetailList: MessageModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        binding.frameLayoutMess.visibility = View.GONE
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount == 0) {
                binding.frameLayoutMess.visibility = View.GONE
            } else {
                binding.frameLayoutMess.visibility = View.VISIBLE
            }
        }
        addIcon()
        infoUserChat()
        backHome()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        database.reference.child("Users")
            .child(firebaseAuth.uid ?: "").child("presence").setValue("Online")
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
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            finish()
        }
    }

    private fun infoUserProfile(name: String?, imgProfile: String?, uidUser: String?) {
        binding.tvNameMess.text = name
        Glide.with(this@MessageActivity)
            .load(imgProfile)
            .transform(CenterCrop(), RoundedCorners(55))
            .placeholder(R.drawable.profile)
            .into(binding.ivImgProfile)
        database.reference.child("Users").child(uidUser.toString()).child("presence")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvPresence.text = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun sendMessage() {
        binding.etSend.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivSend.isVisible = true
            }
        }
        binding.ivSend.setOnClickListener {
            if (binding.etSend.text.isNotEmpty()) {
                val timeStamp = System.currentTimeMillis()
                fun convertLongToTime(timeNow: Long): String {
                    val date = Date(timeNow)
                    val format = SimpleDateFormat("dd.MM HH:mm")
                    return format.format(date)
                }

                val content = binding.etSend.text.toString()
                val mess = MessageModel(
                    null,
                    content,
                    firebaseAuth.uid,
                    convertLongToTime(timeStamp),
                    null
                )
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["lastMessage"] = content
                hashMap["timeStamp"] = convertLongToTime(timeStamp)
                database.reference.child("Message").child(roomId).push().setValue(mess)
                    .addOnSuccessListener {
                        database.reference.child("Room").child(roomId).updateChildren(hashMap)
                    }
                binding.etSend.text.clear()
            } else {
                Toast.makeText(this, getString(R.string.not_empty_message), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendImageIcon(iconList: ArrayList<IconModel>) {
        binding.ivLibrary.setOnClickListener {
            binding.etSend.clearFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                storagePermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                storagePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.ivLibrary.windowToken, 0)
        }
        binding.ivIcon.setOnClickListener {
            binding.etSend.clearFocus()
            fragmentIcon(roomId, iconList)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.ivLibrary.windowToken, 0)
            if (binding.frameLayoutMess.visibility == View.GONE) {
                binding.frameLayoutMess.visibility = View.VISIBLE
            } else {
                binding.frameLayoutMess.visibility = View.GONE
            }
        }
    }

    private val storagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                galleryFragment(roomId)
                if (binding.frameLayoutMess.visibility == View.GONE) {
                    binding.frameLayoutMess.visibility = View.VISIBLE
                } else {
                    binding.frameLayoutMess.visibility = View.GONE
                }
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun getAllMess(imgProfile: String?) {
        database.reference.child("Message").child(roomId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (snap in snapshot.children) {
                        val content = snap.child("content").getValue(String::class.java)
                        val senderId = snap.child("senderId").getValue(String::class.java)
                        val myUid = firebaseAuth.uid ?: ""
                        val iconName = snap.child("iconName").getValue(String::class.java)
                        val url = snap.child("imgUrl").getValue(String::class.java)
                        val mess = snap.getValue(MessageModel::class.java)
                        imageDetailList = mess!!
                        mess.imgIcon = iconName
                        mess.imgUrl = url
                        if (senderId == myUid) {
                            if (content != null && iconName == null && url == null) {
                                mess.let { messageList.add(ItemMessageRVModel(it, true, 1, "")) }
                            } else {
                                if (iconName != null) {
                                    mess.let {
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
                                    if (url != null) {
                                        mess.let {
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
                            }
                        } else {
                            mess.imgFriend = imgProfile
                            if (content != null) {
                                mess.let { messageList.add(ItemMessageRVModel(it, false, 1, "")) }
                            } else {
                                if (iconName != null) {
                                    mess.let {
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
                                    if (url != null) {
                                        mess.let {
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
                        messageAdapter.notifyDataSetChanged()
                    }
                    binding.rvMesHomeMes.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun galleryFragment(roomId: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val galleryFragment = GalleryFragment()
        galleryFragment.galleryFragmentListener = object : GalleryFragment.GalleryFragmentListener {
            override fun onCancelButtonClicked() {
                binding.frameLayoutMess.visibility = View.GONE
            }

            override fun visibleButton() {
                binding.ivSend.visibility = View.VISIBLE
            }

            override fun notVisibleButton() {
                binding.ivSend.visibility = View.GONE
            }

        }
        val photoBundle = Bundle()
        photoBundle.putString("roomId", roomId)
        galleryFragment.arguments = photoBundle
        fragmentTransaction.replace(R.id.frame_layout_mess, galleryFragment)
        fragmentTransaction.commit()
        fragmentManager.isDestroyed
    }

    private fun fragmentIcon(roomId: String, iconList: ArrayList<IconModel>) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val stickerFragment = StickerFragment()
        val iconBundle = Bundle()
        iconBundle.putString("roomId", roomId)
        iconBundle.putParcelableArrayList("iconList", iconList)
        stickerFragment.arguments = iconBundle
        fragmentTransaction.replace(R.id.frame_layout_mess, stickerFragment)
        fragmentTransaction.commit()
        fragmentManager.isDestroyed
    }

    override fun onBackPressed() {
        if (binding.frameLayoutMess.visibility == View.VISIBLE) {
            binding.frameLayoutMess.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
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
        uidFriend = uidUser.toString()
        val myUid = firebaseAuth.uid ?: ""
        roomId = if (myUid > uidFriend) {
            "$myUid$uidFriend"
        } else {
            "$uidFriend$myUid"
        }

        messageAdapter = MessageAdapter(messageList, object : GetDetail {
            override fun detail(urlImg: String) {
                val dialogBinding: DetailImageBinding = DetailImageBinding.inflate(layoutInflater)
                val dialog = Dialog(this@MessageActivity, R.style.FullscreenDialog)
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.gravity
                dialog.window?.attributes = layoutParams

                dialog.setContentView(dialogBinding.root)
                dialog.setCancelable(true)
                dialogBinding.ivCancel.setOnClickListener {
                    dialog.dismiss()
                }
                Glide.with(this@MessageActivity)
                    .load(imgProfile)
                    .transform(CenterCrop(), RoundedCorners(55))
                    .placeholder(R.drawable.profile)
                    .load(urlImg)
                    .centerInside()
                    .into(dialogBinding.ivDetailIm)

                dialog.show()
            }

        })
        binding.rvMesHomeMes.adapter = messageAdapter
        binding.rvMesHomeMes.setHasFixedSize(true)

        infoUserProfile(name, imgProfile, uidUser)
        getAllMess(imgProfile)
        sendMessage()
        sendImageIcon(iconList)
    }

    private val keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        try {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val height = window.decorView.height
            if (height - r.bottom > height * 0.1399) {
                binding.frameLayoutMess.visibility = View.GONE
                binding.rvMesHomeMes.scrollToPosition(messageList.size - 1)
            } else {
                binding.etSend.clearFocus()
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