import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.training.appchat.R
import com.rikkei.training.appchat.databinding.ItemPhotoBinding
import com.rikkei.training.appchat.ui.tabGallery.PhotoItemClick

class GalleryAdapter(
    private val photoList: ArrayList<String>,
    private val photoItemClick: PhotoItemClick
): RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {
    private val selectedItems = ArrayList<String>()

    class ImageViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String, photoItemClick: PhotoItemClick, selectedItems: ArrayList<String>) {
            binding.tvClick.isVisible = selectedItems.contains(imagePath)
            Glide.with(binding.ivGallery.context)
                .load(imagePath)
                .into(binding.ivGallery)
            binding.ivGallery.setOnClickListener {
                photoItemClick.getPhoto(imagePath)
                if (selectedItems.contains(imagePath)) {
                    binding.tvClick.visibility = View.GONE
                    selectedItems.remove(imagePath)
                } else {
                    binding.tvClick.visibility = View.VISIBLE
                    selectedItems.add(imagePath)
                }
            }
        }
    }

    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_photo, parent, false
            )
        )
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(photoList[position], photoItemClick, selectedItems)
    }
}
