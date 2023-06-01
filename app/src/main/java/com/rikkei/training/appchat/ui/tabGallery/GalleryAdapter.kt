package com.rikkei.training.appchat.ui.tabGallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemPhotoBinding
import kotlin.collections.ArrayList

class GalleryAdapter(
    private val photoList: ArrayList<String>,
    private val photoItemClick: PhotoItemClick
): RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        private val selectedItems = ArrayList<String>()
        fun bind(imagePath: String, photoItemClick: PhotoItemClick) {
            binding.tvClick.isVisible = selectedItems.contains(imagePath)
            Glide.with(binding.ivGallery.context)
                .load(imagePath)
                .into(binding.ivGallery)
            binding.ivGallery.setOnClickListener {
                photoItemClick.getPhoto(imagePath)
                if (binding.tvClick.visibility == View.GONE) {
                    binding.tvClick.visibility = View.VISIBLE
                    selectedItems.add(imagePath)
                } else {
                    binding.tvClick.visibility = View.GONE
                    selectedItems.remove(imagePath)
                }
            }
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
        holder.bind(photoList[position], photoItemClick)
    }
}