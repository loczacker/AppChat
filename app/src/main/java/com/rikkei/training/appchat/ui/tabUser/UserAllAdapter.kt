package com.rikkei.training.appchat.ui.tabUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.ui.tabUser.UserAllAdapter.UserViewHolder
import com.rikkei.training.appchat.databinding.ItemUserAllBinding

class UserAllAdapter(
    private val itemList: ArrayList<ItemRecyclerViewModel>,
    private val itemUsersRecycleView: ItemUsersRecycleView
) :
    RecyclerView.Adapter<UserViewHolder>() {

    class UserViewHolder(private val binding: ItemUserAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemRecyclerViewModel, itemUsersRecycleView: ItemUsersRecycleView) {
            val user = item.user
            binding.txtName.text = user.name
            Glide.with(binding.imgCircleHomeMess.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)

            when (item.statusButton) {
                "not_friend" -> {
                    binding.btnAdd.isVisible = true
                }
                "friend" -> {
                    binding.btnAdd.isVisible = false
                }
                "sent" -> {
                    binding.btnAdd.isVisible = false
                }
                "received" -> {
                    binding.btnAdd.isVisible = true
                }
                else -> {binding.btnAdd.isVisible = true}
            }

            binding.btnAdd.setOnClickListener {
                itemUsersRecycleView.getDetail(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_all, parent, false
            )
        )
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(itemList[position], itemUsersRecycleView)
    }
}