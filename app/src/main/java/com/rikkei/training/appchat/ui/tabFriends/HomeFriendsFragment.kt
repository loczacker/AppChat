package com.rikkei.training.appchat.ui.tabFriends

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
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
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding
import com.rikkei.training.appchat.model.FriendModel
import com.rikkei.training.appchat.model.UsersModel

class HomeFriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val friendSearchList: ArrayList<FriendModel> = arrayListOf()

    private lateinit var searchFriendAdapter: SearchFriendAdapter

    private val requestFriendList: ArrayList<UsersModel> = arrayListOf()

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
        searchFriend()
        setupRecyclerView()
        countNumberList()
    }


    private fun setupRecyclerView() {
        searchFriendAdapter = SearchFriendAdapter(friendSearchList)
        binding.rvSearchFr.adapter = searchFriendAdapter
    }

    private fun searchFriend() {
        buttonListener()
        binding.edSearchFr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edSearchFr.text!!.isEmpty()||friendSearchList.isEmpty()) {
                    binding.layoutNotFound.visibility = View.VISIBLE
                } else {
                    binding.layoutNotFound.visibility = View.GONE
                }
                searchFriendAdapter.clearList()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.edSearchFr.text!!.isEmpty()||friendSearchList.isEmpty()) {
                    binding.layoutNotFound.visibility = View.VISIBLE
                } else {
                    binding.layoutNotFound.visibility = View.GONE
                }
                val searchQuery = p0.toString()
                if (searchQuery.isNotEmpty()) {
                    filter(searchQuery)
                }
            }
        })
    }

    private fun filter(searchQuery: String) {
        friendSearchList.clear()
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
                    if (name.contains(searchQuery)) {
                        friendSearchList.add(friend)
                    }
                    searchFriendAdapter.notifyDataSetChanged()
                    binding.layoutNotFound.isVisible = friendSearchList.isEmpty()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun buttonListener() {
        binding.edSearchFr.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tvClearTextFr.isVisible = true
                binding.ivDeleteTextFr.isVisible = true
                searchFriendAdapter.clearList()
                binding.tabFriends.isVisible = false
                binding.vpFriends.isVisible = false
                binding.rvSearchFr.isVisible = true
            } else {
                binding.edSearchFr.hideKeyboard()
                binding.tabFriends.isVisible = true
                binding.vpFriends.isVisible = true
                searchFriendAdapter.clearList()
                binding.rvSearchFr.isVisible = false
            }
        }

        binding.tvClearTextFr.setOnClickListener {
            binding.tvClearTextFr.isVisible = false
            binding.ivDeleteTextFr.isVisible = false
            binding.edSearchFr.text?.clear()
            binding.layoutNotFound.isVisible = false
            binding.edSearchFr.clearFocus()
        }

        binding.ivDeleteTextFr.setOnClickListener {
            binding.edSearchFr.text?.clear()
            binding.edSearchFr.hideKeyboard()
            searchFriendAdapter.clearList()
        }

        binding.edSearchFr.setOnKeyListener { _, p1, _ ->
            if (p1 == KeyEvent.KEYCODE_DEL) {
                searchFriendAdapter.clearList()
            }
            false
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun countNumberList() {
        val friendsRef = database.reference.child("Friends").child(firebaseAuth.uid ?: "")
        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestFriendList.clear()
                for (snap in snapshot.children) {
                    val presence = snap.child("status").value.toString()
                    if (presence == "received" && snap.exists()) {
                        val userRequest = snap.getValue(UsersModel::class.java)
                        userRequest?.presence = presence
                        userRequest?.let { requestFriendList.add(it) }
                    }
                }
                if (requestFriendList.size > 0) {
                    binding.tabFriends.getTabAt(2)?.orCreateBadge?.number = requestFriendList.size
                } else {
                    binding.tabFriends.getTabAt(2)?.removeBadge()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}