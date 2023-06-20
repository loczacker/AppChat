package com.rikkei.training.appchat.ui.profile

import android.app.Dialog
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
import com.rikkei.training.appchat.databinding.DialogChangeLanguageBinding
import com.rikkei.training.appchat.databinding.DialogChoiceLogOutBinding
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
        }
        binding.tvSignOut.setOnClickListener {
            checkSignOut()
        }
        binding.imgSignOut.setOnClickListener{
            checkSignOut()
        }
        binding.languageCurrent.setOnClickListener {
            changeLanguage()
        }
    }

    private fun changeLanguage() {
        val dialogBinding: DialogChangeLanguageBinding =
            DialogChangeLanguageBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.setContentView(dialogBinding.root)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.apply {
            window?.setLayout(layoutParams.width, layoutParams.height)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }
        dialogBinding.btnOk.setOnClickListener {
        }
        dialogBinding.btnRefuse.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkSignOut() {

        val dialogBinding: DialogChoiceLogOutBinding =
            DialogChoiceLogOutBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.setContentView(dialogBinding.root)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.apply {
            window?.setLayout(layoutParams.width, layoutParams.height)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }
        dialogBinding.btnOk.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finish()
        }
        dialogBinding.btnRefuse.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun readData() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
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

                    } catch (_: Exception) {}
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}