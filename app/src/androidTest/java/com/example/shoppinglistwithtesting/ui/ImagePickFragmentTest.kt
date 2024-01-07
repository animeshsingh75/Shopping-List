package com.example.shoppinglistwithtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppinglistwithtesting.R
import com.example.shoppinglistwithtesting.getOrAwaitValue
import com.example.shoppinglistwithtesting.launchFragmentInHiltContainer
import com.example.shoppinglistwithtesting.repository.FakeShoppingRepositoryAndroidTest
import com.example.shoppinglistwithtesting.ui.adapter.ImageAdapter
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

@HiltAndroidTest
@MediumTest
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory
    //region constants

    //endregion constants

    //region helper fields
    //endregion helper fields

    @Before
    fun setup() {
        hiltRule.inject()
    }

    //region helper methods

    //endregion helper methods


    @Test
    fun clickImage_popBackStackAndSetImageUrl() {
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )
        verify(navController).popBackStack()
        assertThat(testViewModel.currImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }
    //region helper classes

    //endregion helper classes
}