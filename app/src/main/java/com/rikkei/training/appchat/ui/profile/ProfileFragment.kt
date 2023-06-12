package com.rikkei.training.appchat.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentProfileBinding
import com.rikkei.training.appchat.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibChange.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, ChangeProfileFragment()).commit()
            transaction.addToBackStack("FragmentProfileBinding")
        }
        binding.tvSignOut.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(
                Intent(activity, LoginActivity::class.java)
            )
            requireActivity().finish()
        }
    }
    private fun readData() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val img = "${snapshot.child("img").value}"

                    binding.tvNameProfile.text = name
                    binding.tvEmailProfile.text = email

                    try {
                        Glide.with(this@ProfileFragment)
                            .load(img)
                            .placeholder(R.drawable.profile)
                            .into(binding.imgProfileCircle)

                        Glide.with(this@ProfileFragment)
                            .load(img)
                            .placeholder(R.drawable.profile)
                            .into(binding.imgProfile)

                    }
                    catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}