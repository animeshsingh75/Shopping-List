package com.example.shoppinglistwithtesting.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglistwithtesting.data.local.ShoppingItem
import com.example.shoppinglistwithtesting.data.remote.responses.ImageResponse
import com.example.shoppinglistwithtesting.utils.Resource

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>

}