package com.rikkei.training.appchat.ui.roomMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemSearchMessBinding
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.RoomModel
import java.util.ArrayList

private const val ROOM_MESSAGE = 0
private const val SEARCH_MESSAGE = 1

class RoomMessengerAdapter(
    private val roomList: ArrayList<RoomModel>,
    private val ItemClick: ItemClick
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun clearList() {
        roomList.clear()
        notifyDataSetChanged()
    }
    class RoomViewHolder(private val binding: ItemUserRowBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(room: RoomModel, itemClickRecyclerView: ItemClick) {
            binding.txtName.text = room.nameRoom
            binding.tvTime.text = room.timeStamp
            binding.tvLastMess.text = room.lastMessage
            Glide.with(binding.imgCircleHomeMess.context).load(room.imgRoom)
                .placeholder(R.drawable.profile)
                .circleCrop()
                .into(binding.imgCircleHomeMess)
            binding.itemHomeFriends.setOnClickListener {
                itemClickRecyclerView.getRoomInfo(room)
            }
        }
    }

    class SearchMsgRoomHolder(private val binding: ItemSearchMessBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(searchMess: RoomModel, itemClickRecyclerView: ItemClick){
            Glide.with(binding.imgCircleSearchMess.context).load(searchMess.imgRoom)
                .placeholder(R.drawable.profile)
                .circleCrop()
                .into(binding.imgCircleSearchMess)
            binding.tvSearchMess.text = searchMess.contentMess
            binding.tvSearchMessName.text = searchMess.nameRoom
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ROOM_MESSAGE -> RoomViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_user_row, parent, false
                )
            )
            SEARCH_MESSAGE -> SearchMsgRoomHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_mess, parent, false
                )
            )
            else -> throw  IllegalArgumentException("Invalid item view type")
        }
    }

    override fun getItemViewType(position: Int): Int = if (roomList[position].typeRoomMess) {
        ROOM_MESSAGE
    } else {
        SEARCH_MESSAGE
    }

    override fun getItemCount(): Int = roomList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val room = roomList[position]
        when(holder) {
            is RoomViewHolder -> holder.bind(room, ItemClick)
            is SearchMsgRoomHolder -> holder.bind(room, ItemClick)
            else ->throw IllegalArgumentException("Invalid ViewHolder")
        }
    }
}