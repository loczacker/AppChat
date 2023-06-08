package com.rikkei.training.appchat.ui.tabFriends

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.appchat.R
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

    private val friendSearchList: ArrayList<FriendModel> = arrayListOf()

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
        TabLayoutMediator(binding.tabFriends, binding.vpFriends) { tab, position ->
            when (position) {
                0 -> tab.text = "BẠN BÈ"
                1 -> tab.text = "TẤT CẢ"
                2 -> tab.text = "YÊU CẦU"
            }
        }.attach()

        when (binding.vpFriends.currentItem) {
            0 -> {
                searchFriend()
            }
        }

    }

    private fun searchFriend() {

        buttonListener()
        binding.edSearchFr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchQuery = p0.toString()
                if (searchQuery.isNotEmpty()) {
                    filter(searchQuery)
                }
            }
        })
    }

    private fun filter(searchQuery: String) {
        val friendData = database.getReference("Friends")
        friendData.child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (friendSnapshot in snapshot.children) {
                        val friendUid = friendSnapshot.key.toString()
                        val status = friendSnapshot.child("status").value.toString()
                        if (status == "friend") {
                            checkFriend(friendUid, searchQuery)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
    private fun checkFriend(
        friendUid: String,
        searchQuery: String
    ) {
        database.reference.child("Users").child(friendUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val img = snapshot.child("img").value.toString()
                    val friend = FriendModel(name = name, img = img)
                    if (name.contains(searchQuery) || name == searchQuery) {
                        friendSearchList.add(friend)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun buttonListener() {
        binding.edSearchFr.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.tvClearTextFr.isVisible = true
                binding.ivDeleteTextFr.isVisible = true
                binding.layoutSearchFriend.isVisible = true
                binding.vpFriends.isVisible = false
                searchFriendFragment()
            } else {
                binding.layoutSearchFriend.hideKeyboard()
            }
        }

        binding.tvClearTextFr.setOnClickListener {
            binding.tvClearTextFr.isVisible = false
            binding.edSearchFr.clearFocus()
            binding.ivDeleteTextFr.isVisible = false
            binding.layoutSearchFriend.hideKeyboard()
        }

        binding.ivDeleteTextFr.setOnClickListener{
            binding.edSearchFr.text?.clear()
            binding.edSearchFr.hideKeyboard()
            binding.ivDeleteTextFr.isVisible = false
            binding.edSearchFr.clearFocus()
        }
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun searchFriendFragment() {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val searchFriendFragment = SearchFriendFragment()
        val searchFriendBundle = Bundle()
        searchFriendBundle.putParcelableArrayList("friendSearchList", friendSearchList)
        searchFriendFragment.arguments = searchFriendBundle
        fragmentTransaction.replace(R.id.layout_search_fr, searchFriendFragment)
        fragmentTransaction.commit()
    }


}