package com.rikkei.training.appchat.ui.tabRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserRequestBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class RequestFriendsAdapter(
    private val requestList: ArrayList<UsersModel>,
    private val itemUsersRecycleView: ItemUsersRecycleView
): RecyclerView.Adapter<RequestFriendsAdapter.RequestViewHolder>() {

    class RequestViewHolder(private val binding: ItemUserRequestBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(user: UsersModel, itemUsersRecycleView: ItemUsersRecycleView){
            binding.txtNameRequest.text = user.name
            Glide.with(binding.imgCircleHomeRequest.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeRequest)

            binding.btnCancel.setOnClickListener {
                itemUsersRecycleView.getDetail(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_request, parent, false
            )
        )
    }

    override fun getItemCount(): Int = requestList.size

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requestList[position], itemUsersRecycleView)
    }
}

