package com.rikkei.training.appchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.adapter.FriendsPagerAdapter
import com.rikkei.training.appchat.adapter.PagerAdapter
import com.rikkei.training.appchat.databinding.FragmentFriendsBinding
import com.rikkei.training.appchat.databinding.ItemUserFriendsBinding

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: PagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tabLayout.addTab(tabLayout.newTab().setText("BẠN BÈ"))
        tabLayout.addTab(tabLayout.newTab().setText("TẤT CẢ"))
        tabLayout.addTab(tabLayout.newTab().setText("YÊU CẦU"))

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

}