package com.rikkei.training.appchat.ui.tabFriends

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding
import com.rikkei.training.appchat.model.FriendModel

class TabFriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FriendsPagerAdapter(childFragmentManager, lifecycle)
        binding.vpFriends.adapter = adapter
        TabLayoutMediator(binding.tabFriends, binding.vpFriends){tab, position ->
            when(position) {
                0 -> tab.text = "BẠN BÈ"
                1 -> tab.text = "TẤT CẢ"
                2 -> tab.text = "YÊU CẦU"
            }
        }.attach()
        searchFriend()
    }

    private fun searchFriend() {
        binding.layoutSearchFriend.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.tvClearTextFr.isVisible = true
                binding.layoutNotFound.isVisible = true
//                listRoom.clear()
            } else {
                binding.layoutSearchFriend.hideKeyboard()
            }
        }

        binding.tvClearTextFr.setOnClickListener{
            binding.tvClearTextFr.isVisible = false
            binding.layoutNotFound.isVisible = false
//            showRoomInfo()
            binding.layoutSearchFriend.clearFocus()
        }

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun searchFriend(query: String?) {
        val friendRef = database.reference.child("Friends").child(firebaseAuth.uid?:"")
        val searchQuery = friendRef.orderByChild("friend").startAt(query).endAt(query + "\uf8ff")
        searchQuery.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

}