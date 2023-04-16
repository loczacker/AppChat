package com.rikkei.training.appchat.ui.tabRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemUserSendRequestBinding
import com.rikkei.training.appchat.model.UsersModel
import com.rikkei.training.appchat.ui.tabUser.ItemUsersRecycleView

class RequestSendFriendsAdapter(
    private val requestSendUserList: ArrayList<UsersModel>,
    private val itemUsersRecycleView: ItemUsersRecycleView
): RecyclerView.Adapter<RequestSendFriendsAdapter.RequestSendViewHolder>() {

    class RequestSendViewHolder(private val binding: ItemUserSendRequestBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(user: UsersModel, itemUsersRecycleView: ItemUsersRecycleView){
            binding.txtNameRequestSend.text = user.name
            Glide.with(binding.imgCircleHomeRequestSend.context).load(user.img)
                .placeholder(R.drawable.profile)
                .into(binding.imgCircleHomeRequestSend)

            binding.btnCancel.setOnClickListener {
                itemUsersRecycleView.getDetail(user)
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
        holder.bind(requestSendUserList[position], itemUsersRecycleView)
    }

    override fun getItemCount(): Int = requestSendUserList.size

}