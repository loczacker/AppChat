package com.rikkei.training.appchat.ui.Messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView
import java.util.ArrayList

class RoomMessengerAdapter(
    private val roomList: ArrayList<UsersModel>,
    private val itemRoom: ItemUsersRecycleView
): RecyclerView.Adapter<RoomMessengerAdapter.RoomViewHolder>() {
    class RoomViewHolder(private val binding: ItemUserRowBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(user: UsersModel, itemUsersRecycleView: ItemUsersRecycleView) {
            binding.txtName.text = user.name
            Glide.with(binding.imgCircleHomeMess.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)
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
        holder.bind(roomList[position], itemRoom)
    }
}