package com.rikkei.training.appchat.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.FragmentChangeProfileBinding


class ChangeProfileFragment : Fragment() {

    private lateinit var binding: FragmentChangeProfileBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    private var imageUri: Uri ?= null

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        //handle click, pick image form camera/gallery
        binding.imgCamera.setOnClickListener {
            showImageAttachMenu()
        }

        //handle click, begin update profile
        binding.txtDone.setOnClickListener {
            validateData()

        }

        binding.imgBackHomeProfile.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, ProfileFragment())?.commit()
        }

        return binding.root

    }

    private var name = ""
    private var phone = ""
    private var birthday = ""

    private fun validateData() {
        name = binding.edtChangeName.text.toString().trim()
        phone = binding.edPhoneChange.text.toString().trim()
        birthday = binding.edBirthdayChange.text.toString().trim()

        if (name.isEmpty()&&phone.isEmpty()&&birthday.isEmpty()) {
            Toast.makeText(activity, "Enter name", Toast.LENGTH_SHORT).show()
        }
        else {
            //name is entered

            if (imageUri == null){
                // update without image
                updateProfile("")
            }else {
                //update with image
                uploadImage()
            }
        }
    }

    private fun updateProfile(uploadedImageUrl: String) {

        progressDialog.setMessage("Updating profile...")
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["name"] = "$name"
        hashMap["phone"] = "$phone"
        hashMap["birthday"] = "$birthday"
        if (imageUri!=null){
            hashMap["img"] = uploadedImageUrl
        }


        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Profile update ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to upload profile due ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()

        val  filePathAndName = "img/"+firebaseAuth.uid

        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot ->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadImageUrl = "${uriTask.result}"

                updateProfile(uploadImageUrl)

            }
            .addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to upload image due ", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun loadUserInfo() {
        //db reference to load user info
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val name = "${snapshot.child("name").value}"
                    val img = "${snapshot.child("img").value}"
                    val birthday = "${snapshot.child("birthday").value}"
                    val phone = "${snapshot.child("phone").value}"

                    binding.edtChangeName.setText("$name")
                    binding.edPhoneChange.setText("$phone")
                    binding.edBirthdayChange.setText("$birthday")

                    try {

                        Glide.with(this@ChangeProfileFragment)
                            .load(img)
                            .placeholder(R.drawable.profile)
                            .into(binding.imgAva)

                    }
                    catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun showImageAttachMenu() {

        val popupMenu = PopupMenu(activity,binding.imgCamera)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()

        //handle popup menu item click

        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                // camera clicked
                pickImageCamera()
            }
            else if (id == 1) {
                //Gallery clicked
                picImageGallery()
            }

            true
        }
    }

    private fun picImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }

    private fun pickImageCamera() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")
        var resolver = requireActivity().contentResolver
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)

    }


    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            //used to handle result of camera intent
            if (result.resultCode == Activity.RESULT_OK) {
                //set to imageview
                binding.imgAva.setImageURI(imageUri)
            }
            else {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data
                //set to imageview
                binding.imgAva.setImageURI(imageUri)
            }
            else {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )


}