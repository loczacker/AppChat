package com.rikkei.training.appchat.ui.roomMessage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.appchat.databinding.FragmentMessengerBinding
import com.rikkei.training.appchat.model.RoomModel
import java.util.ArrayList

class FragmentRoomMessage : Fragment() {

    private lateinit var binding: FragmentMessengerBinding

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
        binding = FragmentMessengerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRoomInfo()
    }

    private fun showRoomInfo() {
        roomAdapter = RoomMessengerAdapter(listRoom, object : RoomItem{
            override fun getRoomInfo(roomModel: RoomModel) {
            }
        })

        binding.rvMesHomeMes.adapter = roomAdapter
    }
}