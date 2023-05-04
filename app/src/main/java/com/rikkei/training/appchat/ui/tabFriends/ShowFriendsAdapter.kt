package com.rikkei.training.appchat.ui.tabFriends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserFriendRowBinding
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.ItemRecyclerViewModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class ShowFriendsAdapter(
    private val friendsList: ArrayList<ItemRecyclerViewModel>,
    private val itemUsersRecycleView: ItemUsersRecycleView
) :
    RecyclerView.Adapter<ShowFriendsAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(private val binding: ItemUserFriendRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemRecyclerViewModel, itemUsersRecycleView: ItemUsersRecycleView) {
            val user = item.user
            binding.tvName.text = user.name
            Glide.with(binding.imgCircleHomeMess.context)
                .load(user.img)
                .transform(CenterCrop(), RoundedCorners(65))
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)

            binding.root.setOnClickListener { itemUsersRecycleView.getDetail(item)  }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_friend_row, parent, false
            )
        )
    }


    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friendsList[position], itemUsersRecycleView)
    }
}