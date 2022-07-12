package gentle.hilt.learnunittests2.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gentle.hilt.learnunittests2.MainCoroutineRule
import gentle.hilt.learnunittests2.R
import gentle.hilt.learnunittests2.db.ShoppingItem
import gentle.hilt.learnunittests2.getOrAwaitValue
import gentle.hilt.learnunittests2.launchFragmentInHiltContainer
import gentle.hilt.learnunittests2.repository.FakeDefaultShoppingRepository
import gentle.hilt.learnunittests2.ui.ShoppingFragmentFactory
import gentle.hilt.learnunittests2.ui.ShoppingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class AddShoppingItemFragmentTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var shoppingFragmentFactory: ShoppingFragmentFactory



    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeDefaultShoppingRepository())

        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun clickIvShoppingImageButton_navigateToImagePickFragment(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(navController).navigate(R.id.action_addShoppingItemFragment_to_pickImageFragment)
    }

    @Test
    fun emptyImageUrlAfterPressingBackButton(){
        val navController = mock(NavController::class.java)
        val fakeViewModel = viewModel
        val url = "url"

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            fakeViewModel.setCurrentImageUrl(url)
            viewModel = fakeViewModel

        }
        pressBack()

        assertThat(fakeViewModel.currentImageUrl.getOrAwaitValue()).isEqualTo("")

    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb(){
        val fakeViewModel = viewModel
        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = shoppingFragmentFactory
        ) {
            viewModel = fakeViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))

        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(fakeViewModel.liveDataShoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("shopping item", 5, 5.5f, ""))
    }


}