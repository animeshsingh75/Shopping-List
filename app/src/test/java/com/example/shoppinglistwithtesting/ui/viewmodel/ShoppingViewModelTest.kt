package com.example.shoppinglistwithtesting.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppinglistwithtesting.MainCoroutineRule
import com.example.shoppinglistwithtesting.getOrAwaitValueTest
import com.example.shoppinglistwithtesting.repository.FakeShoppingRepository
import com.example.shoppinglistwithtesting.utils.Constants
import com.example.shoppinglistwithtesting.utils.Resource
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    //region constants

    //endregion constants

    //region helper fields
    private lateinit var SUT: ShoppingViewModel
    //endregion helper fields

    @Before
    fun setup() {
        SUT = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty fields, returns error`() {
        SUT.insertShoppingItem("name", "", "3.0")
        val value = SUT.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        SUT.insertShoppingItem(string, "5", "3.0")
        val value = SUT.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        SUT.insertShoppingItem("name", "5", string)
        val value = SUT.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }


    @Test
    fun `insert shopping item with too high amount, returns error`() {
        SUT.insertShoppingItem("name", "9999999999999999999", "3.0")
        val value = SUT.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        SUT.insertShoppingItem("name", "5", "3.0")
        val value = SUT.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

    //region helper methods

    //endregion helper methods

    //region helper classes

    //endregion helper classes
}