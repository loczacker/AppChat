package com.rikkei.training.appchat.ui.Messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.SendMsgBinding
import com.rikkei.training.appchat.model.MessengerModel
import com.rikkei.training.appchat.ui.tabRequest.RequestFriendsSentAdapter
import com.rikkei.training.appchat.ui.tabUser.ItemRecyclerViewModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class MessengerAdapter(
    private var messenger: ArrayList<MessengerModel>,
    private var senderRoom: String,
    private var receiverRoom: String,
    private val itemUsersRecycleView: ItemUsersRecycleView
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMsgHolder(private val binding: SendMsgBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: ItemRecyclerViewModel, itemUsersRecycleView: ItemUsersRecycleView){
        }
    }

    class ReceiveMsgHolder(private val binding: ReceiveMsgHolder):
        RecyclerView.ViewHolder(binding.itemView){

        fun bind(item: ItemRecyclerViewModel, itemUsersRecycleView: ItemUsersRecycleView){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestFriendsSentAdapter.RequestViewHolder {
        return RequestFriendsSentAdapter.RequestViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_request, parent, false
            )
        )
    }

    override fun getItemCount(): Int = messenger.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }
}

