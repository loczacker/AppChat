package com.rikkei.training.appchat.ui.tabFriends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemSearchFriendBinding
import com.rikkei.training.appchat.model.FriendModel
import java.util.ArrayList

class SearchFriendAdapter(
    private val searchList: ArrayList<FriendModel>?,
) :
    RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder>() {
    class SearchFriendViewHolder(private val binding: ItemSearchFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendModel) {
            binding.tvSearchFriend.text = item.name
            Glide.with(binding.imgCircleSearchFriend.context)
                .load(item.img)
                .transform(CenterCrop(), RoundedCorners(65))
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleSearchFriend)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendViewHolder {
        return SearchFriendViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_search_friend, parent, false
            )
        )
    }


    override fun getItemCount(): Int = searchList!!.size

    override fun onBindViewHolder(holder: SearchFriendViewHolder, position: Int) {
        holder.bind(searchList!![position])
    }
}