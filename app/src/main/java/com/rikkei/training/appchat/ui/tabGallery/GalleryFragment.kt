package com.rikkei.training.appchat.ui.tabGallery

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.appchat.databinding.FragmentPhotoLibraryBinding
import com.rikkei.training.appchat.model.MessageModel

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentPhotoLibraryBinding

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var galleryRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                }
            }

        val imagesList = getAllImagesFromGallery()
        galleryAdapter = GalleryAdapter(imagesList)

        galleryRecyclerView = binding.recyclerGallery
        galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        galleryRecyclerView.adapter = galleryAdapter

    }

    private fun getAllImagesFromGallery(): ArrayList<String> {
        val imagesList = arrayListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
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
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                imagesList.add(path)
            }
        }

        return imagesList
    }
}
