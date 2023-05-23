package com.rikkei.training.appchat.ui.tabIcon

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentIconBinding
import com.rikkei.training.appchat.ui.message.MessageModel
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class FragmentIcon : Fragment() {

    private lateinit var binding: FragmentIconBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var iconAdapter: IconAdapter

    private lateinit var iconList : ArrayList<IconModel>


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
        iconList = ArrayList()
        iconList.add(IconModel(R.drawable.dumbbell, "dumbbell"))
        iconList.add(IconModel(R.drawable.great_job_good_job_sticker_collection, "great_job_good_job_sticker_collection"))
        iconList.add(IconModel(R.drawable.online_training, "online_training"))
        iconList.add(IconModel(R.drawable.play_with_pet, "play_with_pet"))
        iconList.add(IconModel(R.drawable.reading, "reading"))
        iconList.add(IconModel(R.drawable.stay_at_home, "stay_at_home"))
        iconList.add(IconModel(R.drawable.tea_time, "tea_time"))
        iconList.add(IconModel(R.drawable.video_calling, "video_calling"))
        iconList.add(IconModel(R.drawable.watering_plants, "watering_plants"))
        showIcon()
    }

    private fun showIcon() {
        iconAdapter = IconAdapter(iconList, object : IconItemInterface{
            override fun getIcon(icon: IconModel) {

//                val uidUser = intent.getStringExtra("uid")
//                    fun convertLongToTime(timeNow: Long): String {
//                        val date = Date(timeNow)
//                        val format = SimpleDateFormat("dd.MM HH:mm")
//                        return format.format(date)
//                    }
//                    val mess = MessageModel(null,, firebaseAuth.uid, convertLongToTime(timeStamp),null)
//                    database.reference.child("Message").child(roomId).push().setValue(mess)
//                    binding.etSend.text.clear()
            }
        })
        binding.recyclerIcon.layoutManager = GridLayoutManager(activity,3)
        binding.recyclerIcon.adapter = iconAdapter
        iconAdapter.notifyDataSetChanged()
    }

}