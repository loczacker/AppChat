package com.rikkei.training.appchat.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.databinding.ItemReceivedMsgBinding
import com.rikkei.training.appchat.databinding.ItemSendMsgBinding

class MessageAdapter(
    private var messenger: ArrayList<MessageRecyclerViewModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val FIRST_VIEW = 0
    private val SECOND_VIEW = 1


    class SentMsgHolder(private val binding: ItemSendMsgBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(messSend: MessageRecyclerViewModel){
        }
    }

    class ReceiveMsgHolder(
        private val binding: ItemReceivedMsgBinding
    ):
        RecyclerView.ViewHolder(binding.root){

        fun bind(messReceived: MessageRecyclerViewModel){
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

