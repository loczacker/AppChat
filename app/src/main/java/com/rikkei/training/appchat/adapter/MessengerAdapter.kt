package com.rikkei.training.appchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.Users
import com.rikkei.training.appchat.databinding.ItemUserAllBinding

class MessengerAdapter(var context: Context, var userList:ArrayList<Users>):
    RecyclerView.Adapter<MessengerAdapter.UserViewHolder>() {
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding: ItemUserAllBinding = ItemUserAllBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.item_user_all,
        parent, false)
        return UserViewHolder(v)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var user = userList[position]
        holder.binding.txtName.text = user.name
        Glide.with(context).load(user.img_profile)
            .placeholder(R.drawable.profile)
            .into(holder.binding.imgCircleHomeMess)
    }
}