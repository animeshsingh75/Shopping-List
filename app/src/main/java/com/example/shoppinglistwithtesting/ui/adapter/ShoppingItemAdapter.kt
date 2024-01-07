package com.example.shoppinglistwithtesting.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.shoppinglistwithtesting.data.local.ShoppingItem
import com.example.shoppinglistwithtesting.databinding.ItemShoppingBinding
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(private val glide: RequestManager) :
    RecyclerView.Adapter<ShoppingItemAdapter.ShoppingImageViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingImageViewHolder {
        val binding =
            ItemShoppingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ShoppingImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }


    override fun onBindViewHolder(holder: ShoppingImageViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.bind(shoppingItem, glide)
    }


    class ShoppingImageViewHolder(private val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shoppingItem: ShoppingItem, glide: RequestManager) {
            glide.load(shoppingItem.imageUrl).into(binding.ivShoppingImage)
            binding.tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            binding.tvShoppingItemAmount.text = amountText
            val priceText = "$${shoppingItem.price}"
            binding.tvShoppingItemPrice.text = priceText
        }
    }
}