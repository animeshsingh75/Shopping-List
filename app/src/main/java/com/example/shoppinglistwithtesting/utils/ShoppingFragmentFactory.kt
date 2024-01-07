package com.example.shoppinglistwithtesting.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.example.shoppinglistwithtesting.ui.AddShoppingFragment
import com.example.shoppinglistwithtesting.ui.ImagePickFragment
import com.example.shoppinglistwithtesting.ui.ShoppingFragment
import com.example.shoppinglistwithtesting.ui.adapter.ImageAdapter
import com.example.shoppinglistwithtesting.ui.adapter.ShoppingItemAdapter
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingFragment::class.java.name -> AddShoppingFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter)
            else -> super.instantiate(classLoader, className)
        }

    }
}