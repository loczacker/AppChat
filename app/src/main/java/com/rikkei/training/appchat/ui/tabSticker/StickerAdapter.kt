package com.rikkei.training.appchat.ui.tabSticker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemIconBinding
import com.rikkei.training.appchat.model.IconModel
import java.util.ArrayList

class StickerAdapter(
    private val iconList: ArrayList<IconModel>,
    private val clickItemListener: ClickItemListener
) : RecyclerView.Adapter<StickerAdapter.IconViewHolder>() {
    class IconViewHolder(private val binding: ItemIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: IconModel, clickItemListener: ClickItemListener) {
            Glide.with(binding.ivSticker).load(icon.iconSource)
                .placeholder(R.drawable.profile)
                .into(binding.ivSticker)

            itemView.setOnClickListener {
                clickItemListener.onItemCLick(icon, icon.iconName.toString())
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        return IconViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_icon, parent, false
            )
        )
    }
    override fun getItemCount(): Int = iconList.size

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(iconList[position], clickItemListener)
    }
}