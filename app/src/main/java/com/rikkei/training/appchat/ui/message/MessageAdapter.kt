package com.rikkei.training.appchat.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ReceivedMsgBinding
import com.rikkei.training.appchat.databinding.SendMsgBinding
import com.rikkei.training.appchat.model.MessageRecyclerViewModel

class MessageAdapter(
    private var messenger: ArrayList<MessageRecyclerViewModel>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val FIRST_VIEW = 0
    private val SECOND_VIEW = 1


    class SentMsgHolder(private val binding: SendMsgBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(messSend: MessageRecyclerViewModel){
            binding.tvMyMessage.text = messSend.message.content
            binding.tvTimeSend.text = messSend.message.time
        }
    }

    class ReceiveMsgHolder(private val binding: ReceivedMsgBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(messReceived: MessageRecyclerViewModel){
            binding.tvMess.text = messReceived.message.content
            binding.tvTimeReceived.text = messReceived.message.time
            Glide.with(binding.imMessChat.context).load(messReceived.message.imgFriend)
                .placeholder(R.drawable.profile)
                .into(binding.imMessChat)
        }
    }

    override fun getItemViewType(position: Int): Int = when (messenger[position].messageType) {
        FIRST_VIEW -> FIRST_VIEW
        SECOND_VIEW-> SECOND_VIEW
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FIRST_VIEW -> SentMsgHolder(SendMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            SECOND_VIEW -> ReceiveMsgHolder(ReceivedMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("invalid item type")
        }
    }

    override fun getItemCount(): Int = messenger.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(messenger[position].messageType) {
            FIRST_VIEW -> (holder as SentMsgHolder).bind(messenger[position])
            SECOND_VIEW -> (holder as ReceiveMsgHolder).bind(messenger[position])
            else -> throw IllegalArgumentException("invalid item type")
        }
    }

}

