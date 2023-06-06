package com.rikkei.training.appchat.ui.roomMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemSearchMessBinding
import com.rikkei.training.appchat.model.MessageModel
import java.util.ArrayList

class SearchMessAdapter(
    private val messList: ArrayList<MessageModel>
): RecyclerView.Adapter<SearchMessAdapter.SearchMessViewHolder>() {
    class SearchMessViewHolder(private val binding: ItemSearchMessBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(mess: MessageModel) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMessViewHolder {
        return SearchMessViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_search_mess, parent, false
            )
        )
    }

    override fun getItemCount(): Int = messList.size

    override fun onBindViewHolder(holder: SearchMessViewHolder, position: Int) {
        holder.bind(messList[position])
    }
}