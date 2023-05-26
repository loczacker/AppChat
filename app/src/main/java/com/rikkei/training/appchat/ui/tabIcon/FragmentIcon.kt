package com.rikkei.training.appchat.ui.tabIcon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.databinding.FragmentIconBinding
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
        val bundle = arguments
        val roomId = bundle!!.getString("roomId")
        val receivedArrayList = bundle.getParcelableArrayList<IconModel>("iconList")
        if (receivedArrayList != null){
            iconList.addAll(receivedArrayList)
        }
        showIcon(roomId)
    }

    private fun showIcon(roomId: String?) {
        iconAdapter = IconAdapter(iconList, object : IconItemInterface{

            override fun getIcon(iconModel: IconModel, iconName: String) {

                val timeStamp = System.currentTimeMillis()

                fun convertLongToTime(timeNow: Long): String {
                    val date = Date(timeNow)
                    val format = SimpleDateFormat("dd.MM HH:mm")
                    return format.format(date)
                }

                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["senderId"] = firebaseAuth.uid?:""
                hashMap["time"] = convertLongToTime(timeStamp)
                hashMap["iconName"] = iconName
                database.reference.child("Message").child(roomId.toString()).push().updateChildren(hashMap)
            }
        })
        binding.recyclerIcon.layoutManager = GridLayoutManager(activity,3)
        binding.recyclerIcon.adapter = iconAdapter
        iconAdapter.notifyDataSetChanged()
    }

}