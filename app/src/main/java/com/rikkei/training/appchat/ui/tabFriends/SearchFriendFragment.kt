package com.rikkei.training.appchat.ui.tabFriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.appchat.databinding.FragmentSearchFriendBinding
import com.rikkei.training.appchat.model.FriendModel

class SearchFriendFragment : Fragment() {

    private lateinit var binding : FragmentSearchFriendBinding
    private lateinit var searchFriendAdapter: SearchFriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val receivedArrayList = bundle?.getParcelableArrayList<FriendModel>("friendSearchList")

        searchFriendAdapter = SearchFriendAdapter(receivedArrayList)
        binding.recyclerViewTabSearchFriend.adapter = searchFriendAdapter
    }
}