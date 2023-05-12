package com.rikkei.training.appchat.ui.roomMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.RoomModel
import java.util.ArrayList

class RoomMessengerAdapter(
    private val roomList: ArrayList<RoomModel>,
    private val roomItem: RoomItem
): RecyclerView.Adapter<RoomMessengerAdapter.RoomViewHolder>() {
    class RoomViewHolder(private val binding: ItemUserRowBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(room: RoomModel, roomItemRecyclerView: RoomItem) {
            binding.txtUnreadMessage.text = room.unreadMessage.toString()
            binding.tvTime.text = room.timeStamp
            binding.txtLastMessage.text = room.lastMessage
            Glide.with(binding.imgCircleHomeMess.context).load(room)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)
            binding.itemHomeFriends.setOnClickListener {
                roomItemRecyclerView.getRoomInfo(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_row, parent, false
            )
        )
    }

    override fun getItemCount(): Int = roomList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(roomList[position], roomItem)
    }
}