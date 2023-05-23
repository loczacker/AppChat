package com.rikkei.training.appchat.ui.tabPhoto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemPhotoBinding
import com.rikkei.training.appchat.ui.roomMessage.RoomModel
import com.rikkei.training.appchat.ui.roomMessage.RoomItem
import java.util.ArrayList

class GalleryAdapter(
    private val roomList: ArrayList<RoomModel>,
    private val roomItem: RoomItem
): RecyclerView.Adapter<GalleryAdapter.RoomViewHolder>() {
    class RoomViewHolder(private val binding: ItemPhotoBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(room: RoomModel, roomItemRecyclerView: RoomItem) {

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