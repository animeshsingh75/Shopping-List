package com.example.shoppinglistwithtesting.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglistwithtesting.data.local.ShoppingDao
import com.example.shoppinglistwithtesting.data.local.ShoppingItem
import com.example.shoppinglistwithtesting.data.remote.PixabayApi
import com.example.shoppinglistwithtesting.data.remote.responses.ImageResponse
import com.example.shoppinglistwithtesting.utils.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : BaseRepo(), ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return safeApiCall { pixabayApi.searchForImage(imageQuery) }
    }

}