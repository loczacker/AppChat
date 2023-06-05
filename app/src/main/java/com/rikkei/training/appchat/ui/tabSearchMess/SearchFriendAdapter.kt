package com.rikkei.training.appchat.ui.tabSearchMess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemSearchFriendBinding
import com.rikkei.training.appchat.databinding.ItemUserRowBinding
import com.rikkei.training.appchat.model.FriendModel
import java.util.ArrayList

class SearchFriendAdapter(
    private val friendList: ArrayList<FriendModel>
): RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder>() {
    class SearchFriendViewHolder(private val binding: ItemSearchFriendBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(friend: FriendModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendAdapter.SearchFriendViewHolder {
        return  SearchFriendAdapter.SearchFriendViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_search_friend, parent, false
            )
        )
    }

    override fun getItemCount(): Int = friendList.size

    override fun onBindViewHolder(holder: SearchFriendAdapter.SearchFriendViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}