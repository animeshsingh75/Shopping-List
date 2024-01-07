package com.example.shoppinglistwithtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppinglistwithtesting.R
import com.example.shoppinglistwithtesting.data.local.ShoppingItem
import com.example.shoppinglistwithtesting.getOrAwaitValue
import com.example.shoppinglistwithtesting.launchFragmentInHiltContainer
import com.example.shoppinglistwithtesting.ui.adapter.ShoppingItemAdapter
import com.example.shoppinglistwithtesting.ui.viewmodel.ShoppingViewModel
import com.example.shoppinglistwithtesting.utils.TestShoppingFragmentFactory
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    //region constants

    //endregion constants

    //region helper fields
    //endregion helper fields

    @Before
    fun setup() {
        hiltRule.inject()
    }

    //region helper methods
    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory

    //endregion helper methods


    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.fabAddShoppingItem)).perform(click())
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingFragment()
        )
    }

    @Test
    fun swipeShoppingItem_deleteItemInDb() {
        val shoppingItem = ShoppingItem("TEST", 1, 1f, "TEST", 1)
        var testViewModel: ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = fragmentFactory) {
            testViewModel = viewModel
            viewModel?.insertShoppingItemToDb(shoppingItem)
        }
        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingImageViewHolder>(
                0,
                swipeLeft()
            )
        )
        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).isEmpty()
    }

    //region helper classes

    //endregion helper classes
}