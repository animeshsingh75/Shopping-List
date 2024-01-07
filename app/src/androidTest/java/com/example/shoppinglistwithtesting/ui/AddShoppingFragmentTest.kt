package com.example.shoppinglistwithtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppinglistwithtesting.R
import com.example.shoppinglistwithtesting.data.local.ShoppingItem
import com.example.shoppinglistwithtesting.getOrAwaitValue
import com.example.shoppinglistwithtesting.launchFragmentInHiltContainer
import com.example.shoppinglistwithtesting.repository.FakeShoppingRepositoryAndroidTest
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
class AddShoppingFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //region constants

    //endregion constants

    //region helper fields
    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory
    //endregion helper fields

    @Before
    fun setup() {
        hiltRule.inject()
    }

    //region helper methods

    //endregion helper methods

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        pressBack()
        verify(navController).popBackStack()
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Shopping Item1"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("Shopping Item1", 5, 5.5f, ""))
    }

    @Test
    fun clickAddShoppingImage_navigateToImagePickFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.ivShoppingImage)).perform(click())
        verify(navController).navigate(
            AddShoppingFragmentDirections.actionAddShoppingFragmentToImagePickFragment()
        )
    }
    //region helper classes

    //endregion helper classes
}