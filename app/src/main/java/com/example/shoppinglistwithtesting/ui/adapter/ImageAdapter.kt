package com.example.shoppinglistwithtesting.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.shoppinglistwithtesting.databinding.ItemImageBinding
import javax.inject.Inject

class ImageAdapter @Inject constructor(private val glide: RequestManager) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<String?>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private var onItemClickListener: ((String?) -> Unit)? = null
    fun setOnItemClickListener(listener: (String?) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        holder.bind(url, glide, onItemClickListener)
    }


    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String?, glide: RequestManager, onItemClickListener: ((String?) -> Unit)?) {
            glide.load(url).into(binding.ivShoppingImage)
            binding.ivShoppingImage.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(url)
                }
            }
        }
    }
}