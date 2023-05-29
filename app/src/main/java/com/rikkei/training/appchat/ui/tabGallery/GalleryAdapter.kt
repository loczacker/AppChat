package com.rikkei.training.appchat.ui.tabGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemPhotoBinding
import com.rikkei.training.appchat.model.MessageModel
import java.util.ArrayList

class GalleryAdapter(
    private val photoList: ArrayList<MessageModel>
): RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemPhotoBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return  ImageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_photo, parent, false
            )
        )
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(photoList[position])
    }
}