package com.rikkei.training.appchat.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemReceivedMsgBinding
import com.rikkei.training.appchat.databinding.ItemSendMsgBinding

class MessageAdapter(
    private var messenger: ArrayList<MessageRecyclerViewModel>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val FIRST_VIEW = 0
    private val SECOND_VIEW = 1


    class SentMsgHolder(private val binding: ItemSendMsgBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(messSend: MessageRecyclerViewModel){
            if (messSend.message.imgUrl.isNullOrEmpty() && messSend.message.imgIcon.isNullOrEmpty())
            {
                binding.tvMyMessage.text = messSend.message.content
                binding.tvTimeSend.text = messSend.message.time
                binding.ivImSent.isVisible = false
            } else if (messSend.message.content.isNullOrEmpty() && messSend.message.imgUrl.isNullOrEmpty())
            {
                Glide.with(binding.ivImSent).load(messSend.message.imgIcon)
                    .placeholder(R.drawable.profile)
                    .into(binding.ivImSent)
                binding.tvTimeSend.text = messSend.message.time
                binding.sentText.isVisible = false
            } else
            {
                Glide.with(binding.ivImSent).load(messSend.message.imgUrl)
                    .placeholder(R.drawable.profile)
                    .into(binding.ivImSent)
                binding.tvTimeSend.text = messSend.message.time
                binding.sentText.isVisible = false
            }
        }
    }

    class ReceiveMsgHolder(
        private val binding: ItemReceivedMsgBinding
    ):
        RecyclerView.ViewHolder(binding.root){

        fun bind(messReceived: MessageRecyclerViewModel){
            if (messReceived.message.imgUrl.isNullOrEmpty() && messReceived.message.imgIcon.isNullOrEmpty())
            {
                binding.tvMess.text = messReceived.message.content
                binding.tvTimeReceived.text = messReceived.message.time
                Glide.with(binding.imMessChat.context).load(messReceived.message.imgFriend)
                    .placeholder(R.drawable.profile)
                    .into(binding.imMessChat)
                binding.ivImReceived.visibility = View.GONE
            } else if (messReceived.message.content.isNullOrEmpty() && messReceived.message.imgUrl.isNullOrEmpty())
            {
                Glide.with(binding.ivImReceived).load(messReceived.message.imgIcon)
                    .placeholder(R.drawable.profile)
                    .into(binding.ivImReceived)
                binding.tvTimeReceived.text = messReceived.message.time
                Glide.with(binding.imMessChat.context).load(messReceived.message.imgFriend)
                    .placeholder(R.drawable.profile)
                    .into(binding.imMessChat)
                binding.tvMess.visibility = View.GONE
            } else
            {
                Glide.with(binding.ivImReceived).load(messReceived.message.imgUrl)
                    .placeholder(R.drawable.profile)
                    .into(binding.ivImReceived)
                binding.tvTimeReceived.text = messReceived.message.time
                Glide.with(binding.imMessChat.context).load(messReceived.message.imgFriend)
                    .placeholder(R.drawable.profile)
                    .into(binding.imMessChat)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (messenger[position].messageType) {
        FIRST_VIEW -> FIRST_VIEW
        SECOND_VIEW-> SECOND_VIEW
        else -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FIRST_VIEW -> SentMsgHolder(ItemSendMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            SECOND_VIEW -> ReceiveMsgHolder(ItemReceivedMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

