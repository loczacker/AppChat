package com.rikkei.training.appchat.ui.tabGallery

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
import com.rikkei.training.appchat.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var galleryRecyclerView: RecyclerView

    var galleryFragmentListener: GalleryFragmentListener? = null
    interface GalleryFragmentListener {
        fun onCancelButtonClicked()
    }

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
        binding.btnSent.isVisible = false
        binding.btnCancel.isVisible = false

        binding.btnCancel.setOnClickListener {
            galleryFragmentListener?.onCancelButtonClicked()
        }


        val imagesList = getAllImagesFromGallery()
        galleryAdapter = GalleryAdapter(imagesList, object : PhotoItem{
            override fun getPhoto(url: String) {
                if (binding.btnSent.visibility == View.GONE || binding.btnSent.visibility == View.GONE)
                {
                    binding.btnSent.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                }
            }

        })

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
