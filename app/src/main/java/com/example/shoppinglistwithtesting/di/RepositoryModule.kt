package com.example.shoppinglistwithtesting.di

import com.example.shoppinglistwithtesting.repository.DefaultShoppingRepository
import com.example.shoppinglistwithtesting.repository.ShoppingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyRepository(defaultShoppingRepository: DefaultShoppingRepository): ShoppingRepository
}