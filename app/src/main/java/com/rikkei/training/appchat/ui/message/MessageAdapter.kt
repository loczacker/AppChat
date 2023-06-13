package com.rikkei.training.appchat.ui.message

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemReceivedMsgBinding
import com.rikkei.training.appchat.databinding.ItemSendMsgBinding
import com.rikkei.training.appchat.model.ItemMessageRVModel
import com.rikkei.training.appchat.model.MessageType

private const val MY_MESSAGE = 0
private const val YOUR_MESSAGE = 1

class MessageAdapter(
    private var messenger: ArrayList<ItemMessageRVModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SentMsgHolder(private val binding: ItemSendMsgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DiscouragedApi")
        fun bind(messSend: ItemMessageRVModel) {
            binding.tvTimeSend.text = messSend.message.time

            when (messSend.messageType) {
                MessageType.TEXT -> {
                    binding.ivMessage.isVisible = true
                    binding.tvMessage.isVisible = true
                    binding.tvMessage.text = messSend.message.content
                    binding.ivMessage.isVisible = false
                }

                MessageType.IMAGE -> {
                    binding.ivMessage.isVisible = true
                    binding.ivMessage.isVisible = true
                    Glide.with(binding.ivMessage.context).load(messSend.message.imgUrl)
                        .placeholder(R.drawable.profile)
                        .into(binding.ivMessage)
                    binding.tvMessage.isVisible = false
                    binding.ivMessage.setOnClickListener{

                    }
                }

                MessageType.ICON -> {
                    binding.ivMessage.isVisible = true
                    binding.ivMessage.isVisible = true
                    val resourceId = binding.ivMessage.context.resources.getIdentifier(
                        messSend.message.imgIcon,
                        "drawable",
                        binding.ivMessage.context.packageName
                    )
                    val drawable = ContextCompat.getDrawable(binding.ivMessage.context, resourceId)
                    binding.ivMessage.setImageDrawable(drawable)
                    binding.tvMessage.isVisible = false
                }
            }
        }
    }

    class ReceiveMsgHolder(private val binding: ItemReceivedMsgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DiscouragedApi")
        fun bind(messReceived: ItemMessageRVModel) {

            Glide.with(binding.imMessChat.context).load(messReceived.message.imgFriend)
                .placeholder(R.drawable.profile)
                .into(binding.imMessChat)

            when (messReceived.messageType) {
                MessageType.TEXT -> {
                    binding.ivImReceived.isVisible = true
                    binding.tvMess.isVisible = true
                    binding.tvMess.text = messReceived.message.content
                    binding.tvTimeReceived.text = messReceived.message.time
                    binding.ivImReceived.isVisible = false
                }

                MessageType.IMAGE -> {
                    binding.ivImReceived.isVisible = true
                    binding.tvMess.isVisible = true
                    Glide.with(binding.ivImReceived.context).load(messReceived.message.imgUrl)
                        .placeholder(R.drawable.profile)
                        .into(binding.ivImReceived)
                    binding.tvTimeReceived.text = messReceived.message.time
                    binding.tvMess.isVisible = false
                }

                MessageType.ICON -> {
                    binding.ivImReceived.isVisible = true
                    binding.tvMess.isVisible = true
                    val resourceId = binding.ivImReceived.context.resources.getIdentifier(
                        messReceived.message.imgIcon,
                        "drawable",
                        binding.ivImReceived.context.packageName
                    )
                    val drawable =
                        ContextCompat.getDrawable(binding.ivImReceived.context, resourceId)
                    binding.ivImReceived.setImageDrawable(drawable)
                    binding.tvTimeReceived.text = messReceived.message.time
                    binding.tvMess.isVisible = false
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (messenger[position].isMyMessage) {
        MY_MESSAGE
    } else {
        YOUR_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_MESSAGE -> SentMsgHolder(
                ItemSendMsgBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            YOUR_MESSAGE -> ReceiveMsgHolder(
                ItemReceivedMsgBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            else -> throw IllegalArgumentException("Invalid item view type")
        }
    }

    override fun getItemCount(): Int = messenger.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messenger[position]
        when (holder) {
            is SentMsgHolder -> holder.bind(message)
            is ReceiveMsgHolder -> holder.bind(message)
            else -> throw IllegalArgumentException("Invalid ViewHolder")
        }
    }

}

