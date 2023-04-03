package com.rikkei.training.appchat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.activity.MainActivity
import com.rikkei.training.appchat.databinding.FragmentProfileBinding
import com.rikkei.training.appchat.login.LoginActivity

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
        binding.ibChange.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, ChangeProfileFragment())?.commit()
        }
        binding.txtSignOut.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(
                Intent(activity, LoginActivity::class.java )
            )
        }
        return binding.root
    }


    private fun readData() {
//        if (firebaseAuth.currentUser?.uid == null){
//            return
//        }
//        val database = FirebaseDatabase.getInstance().getReference("Users")
//        database.child(firebaseAuth.currentUser?.uid!!).get().addOnSuccessListener {
//            val email = it.child("email").value
//            val name = it.child("name").value
//            binding.tvEmailProfile.text = email.toString()
//            binding.tvNameProfile.text = name.toString()
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }

        //db reference to load user info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val img = "${snapshot.child("img").value}"
                    val birthday = "${snapshot.child("birthday").value}"
                    val phone = "${snapshot.child("phone").value}"
                    val uid = "${snapshot.child("uid").value}"

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