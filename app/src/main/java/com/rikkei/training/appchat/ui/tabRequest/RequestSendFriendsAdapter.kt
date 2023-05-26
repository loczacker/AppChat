package com.rikkei.training.appchat.ui.tabRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserSendRequestBinding
import com.rikkei.training.appchat.model.UsersModel

class RequestSendFriendsAdapter(
    private val requestSendUserList: ArrayList<UsersModel>,
    private val itemSentUsersInterface: ItemSentUsersInterface
): RecyclerView.Adapter<RequestSendFriendsAdapter.RequestSendViewHolder>() {

    class RequestSendViewHolder(private val binding: ItemUserSendRequestBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(user: UsersModel, itemUsersRecycleView: ItemSentUsersInterface){
            binding.txtNameRequestSend.text = user.name
            Glide.with(binding.imgCircleHomeRequestSend.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeRequestSend)

            binding.btnAccept.setOnClickListener {
                itemUsersRecycleView.getFriends(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestSendViewHolder {
        return RequestSendViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user_send_request, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RequestSendViewHolder, position: Int) {
        holder.bind(requestSendUserList[position], itemSentUsersInterface)
    }

    override fun getItemCount(): Int = requestSendUserList.size

}