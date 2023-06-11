package com.rikkei.training.appchat.ui.tabSticker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentIconBinding
import com.rikkei.training.appchat.model.IconModel
import java.text.SimpleDateFormat
import java.util.Date

class StickerFragment : Fragment() {

    private lateinit var binding: FragmentIconBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var stickerAdapter: StickerAdapter

    private val iconList = arrayListOf<IconModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIconBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val roomId = bundle!!.getString("roomId")
        val receivedArrayList = bundle.getParcelableArrayList<IconModel>("iconList")
        if (receivedArrayList != null){
            iconList.addAll(receivedArrayList)
        }
        showIcon(roomId)
    }

    private fun showIcon(roomId: String?) {
        stickerAdapter = StickerAdapter(iconList, object : ClickItemListener {
            override fun onItemCLick(iconModel: IconModel, iconName: String) {
                val timeStamp = System.currentTimeMillis()
                fun convertLongToTime(timeNow: Long): String {
                    val date = Date(timeNow)
                    val format = SimpleDateFormat("dd.MM HH:mm")
                    return format.format(date)
                }
                val messageHashMap: HashMap<String, Any> = HashMap()
                messageHashMap["senderId"] = firebaseAuth.uid ?: ""
                messageHashMap["time"] = convertLongToTime(timeStamp)
                messageHashMap["iconName"] = iconName

                val roomHashMap: HashMap<String, Any> = HashMap()
                roomHashMap["senderId"] = firebaseAuth.uid ?: ""
                roomHashMap["lastMessage"] = getString(R.string.sticker)
                roomHashMap["timeStamp"] = convertLongToTime(timeStamp)

                val messageId = database.reference.child("Message").child(roomId.toString()).push().key

                val updates: MutableMap<String, Any> = HashMap()
                updates["Message/$roomId/$messageId"] = messageHashMap
                updates["Room/$roomId"] = roomHashMap

                database.reference.updateChildren(updates)
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {
                    }
            }
        })
        binding.rcvIcon.adapter = stickerAdapter
    }
}
