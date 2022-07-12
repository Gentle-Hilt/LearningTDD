package gentle.hilt.learnunittests2.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gentle.hilt.learnunittests2.R
import gentle.hilt.learnunittests2.adapters.ImageAdapter
import gentle.hilt.learnunittests2.getOrAwaitValue
import gentle.hilt.learnunittests2.launchFragmentInHiltContainer
import gentle.hilt.learnunittests2.remote.PixabayApi
import gentle.hilt.learnunittests2.repository.FakeDefaultShoppingRepository
import gentle.hilt.learnunittests2.ui.ShoppingFragmentFactory
import gentle.hilt.learnunittests2.ui.ShoppingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.http.Query
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class PickImageFragmentTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var shoppingFragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()

    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl(){
        val navController = mock(NavController::class.java)
        val imageUrl = "Test"
        val fakeViewModel = ShoppingViewModel(FakeDefaultShoppingRepository())

        launchFragmentInHiltContainer<PickImageFragment>(fragmentFactory = shoppingFragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)

            viewModel = fakeViewModel
        }

        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ViewHolder>(
                0,
                click()
            )
        )

        verify(navController).popBackStack()
        assertThat(fakeViewModel.currentImageUrl.getOrAwaitValue()).isEqualTo("Test")
    }

}