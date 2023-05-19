package com.rikkei.training.appchat.ui.tabIcon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemIconBinding
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.RoomModel
import com.rikkei.training.appchat.ui.roomMessage.RoomItem
import java.util.ArrayList

class IconAdapter(
    private val iconList: ArrayList<RoomModel>,
): RecyclerView.Adapter<IconAdapter.RoomViewHolder>() {
    class RoomViewHolder(private val binding: ItemIconBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(room: RoomModel) {
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

    override fun getItemCount(): Int = iconList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(iconList[position])
    }
}