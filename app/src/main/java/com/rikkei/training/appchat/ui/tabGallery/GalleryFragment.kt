package com.rikkei.training.appchat.ui.tabGallery

import GalleryAdapter
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.rikkei.training.appchat.databinding.FragmentGalleryBinding
import java.text.SimpleDateFormat
import java.util.Date

class GalleryFragment : Fragment() {

    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var binding: FragmentGalleryBinding

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var galleryRecyclerView: RecyclerView

    private var selectedPhotoList : ArrayList<String> = arrayListOf()

    interface GalleryFragmentListener {
        fun onCancelButtonClicked()

        fun visibleButton()

        fun notVisibleButton()
    }

    var galleryFragmentListener: GalleryFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val roomId = bundle!!.getString("roomId")
        val uidFriend = bundle.getString("uidFriend")
        binding.layoutButton.isVisible = false
        binding.btnCancel.setOnClickListener {
            galleryFragmentListener?.visibleButton()
            galleryFragmentListener?.onCancelButtonClicked()
        }
        galleryAdapter = GalleryAdapter(getAllImagesFromGallery(), object : PhotoItemClick{
            override fun getPhoto(imagePath: String) {
                if (selectedPhotoList.contains(imagePath)) {
                    selectedPhotoList.remove(imagePath)
                } else {
                    selectedPhotoList.add(imagePath)
                }
                updateButtonVisibility()
            }
        })

        galleryRecyclerView = binding.recyclerGallery
        galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        galleryRecyclerView.adapter = galleryAdapter
        updateButtonVisibility()

        binding.btnSent.setOnClickListener {
            uploadImagesToFirebaseStorage(selectedPhotoList, roomId, uidFriend)
            galleryAdapter.clearSelections()
            selectedPhotoList.clear()
            updateButtonVisibility()
        }
    }

    private fun uploadImagesToFirebaseStorage(
        imageList: List<String>,
        roomId: String?,
        uidFriend: String?
    ) {
        val timeStamp = System.currentTimeMillis()
        val messageId = FirebaseDatabase.getInstance().reference.child("Message").push().key
        val storageRef = FirebaseStorage.getInstance().reference

        fun convertLongToTime(timeNow: Long): String {
            val date = Date(timeNow)
            val format = SimpleDateFormat("dd.MM HH:mm")
            return format.format(date)
        }

        imageList.forEachIndexed { index, imagePath ->
            val fileUri = Uri.parse(imagePath)
            val imageRef = storageRef.child("img/${messageId}_$index")

            val uploadTask = imageRef.putFile(fileUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                uriTask.addOnSuccessListener { downloadUri ->
                    val uploadImageUrl = downloadUri.toString()
                    val messageHashMap: HashMap<String, Any> = HashMap()
                    messageHashMap["imgUrl"] = uploadImageUrl
                    messageHashMap["time"] = convertLongToTime(timeStamp)
                    messageHashMap["senderId"] = firebaseAuth.uid ?: ""

                    val roomUpdates: HashMap<String, Any> = HashMap()
                    roomUpdates["lastMessage"] = "Image"
                    roomUpdates["timeStamp"] = convertLongToTime(timeStamp)
                    roomUpdates["senderId"] = firebaseAuth.uid ?: ""

                    database.reference.child("Room").child(roomId.toString()).child("member")
                        .child(uidFriend.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val unreadMessages = snapshot.child("unread messages").getValue(Int::class.java) ?: 0
                                val newCount = unreadMessages + 1
                                database.reference.child("Room").child(roomId.toString()).child("member")
                                    .child(uidFriend.toString()).child("unread messages").setValue(newCount)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })

                    database.reference.child("Message").child(roomId.toString())
                        .push().updateChildren(messageHashMap)
                    database.reference.child("Room").child(roomId.toString())
                        .updateChildren(roomUpdates)

                }
            }.addOnFailureListener {
            }
        }
    }
    private fun getAllImagesFromGallery(): ArrayList<String> {
        val imagesList = arrayListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = requireContext().contentResolver.query(
            queryUri,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(queryUri, id)
                val path = contentUri.toString()
                imagesList.add(path)
            }
        }
        return imagesList
    }

    private fun updateButtonVisibility() {
        if (selectedPhotoList.isEmpty()) {
            binding.layoutButton.visibility = View.GONE
            galleryFragmentListener?.visibleButton()
        } else {
            binding.layoutButton.visibility = View.VISIBLE
            galleryFragmentListener?.notVisibleButton()
        }
    }
}
