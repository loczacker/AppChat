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
import com.rikkei.training.appchat.model.ItemUsersRVModel

class UserAllAdapter(
    private val itemList: ArrayList<ItemUsersRVModel>,
    private val itemUsersRVInterface: ItemUsersRVInterface
) : RecyclerView.Adapter<UserViewHolder>() {

    class UserViewHolder(private val binding: ItemUserAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemUsersRVModel, itemUsersRVInterface: ItemUsersRVInterface) {
            val user = item.user
            binding.tvName.text = user.name
            Glide.with(binding.imgCircleHomeMess.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)

            binding.btnAdd.setOnClickListener {
                itemUsersRVInterface.getDetail(item)
            }

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

                else -> {
                    binding.btnAdd.isVisible = true
                }
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
        holder.bind(itemList[position], itemUsersRVInterface)
    }
}
