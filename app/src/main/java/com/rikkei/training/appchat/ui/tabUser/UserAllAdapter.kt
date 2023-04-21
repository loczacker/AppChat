package com.rikkei.training.appchat.ui.tabUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.UserAllAdapter.UserViewHolder
import com.rikkei.training.appchat.databinding.ItemUserAllBinding
import com.rikkei.training.appchat.databinding.ItemUserRequestBinding

class UserAllAdapter(
    private val userList: ArrayList<UsersModel>,
    private val itemUsersRecycleView: ItemUsersRecycleView
) :

    RecyclerView.Adapter<UserViewHolder>() {
    class UserViewHolder(private val binding: ItemUserAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UsersModel, itemUsersRecycleView: ItemUsersRecycleView) {
            binding.txtName.text = user.name
            Glide.with(binding.imgCircleHomeMess.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeMess)

            binding.btnAdd.setOnClickListener {
                itemUsersRecycleView.getDetail(user)
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

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position], itemUsersRecycleView)
    }
}