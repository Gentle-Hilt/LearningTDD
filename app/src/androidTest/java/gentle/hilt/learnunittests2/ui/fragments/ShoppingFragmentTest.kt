package gentle.hilt.learnunittests2.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gentle.hilt.learnunittests2.R
import gentle.hilt.learnunittests2.TestShoppingFragmentFactory
import gentle.hilt.learnunittests2.adapters.ShoppingItemAdapter
import gentle.hilt.learnunittests2.db.ShoppingItem
import gentle.hilt.learnunittests2.getOrAwaitValue
import gentle.hilt.learnunittests2.launchFragmentInHiltContainer
import gentle.hilt.learnunittests2.ui.ShoppingViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class ShoppingFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testShoppingFragmentFactory: TestShoppingFragmentFactory



    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController: NavController = mockk(relaxed = true)
        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        coVerify {(navController).navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment)}
    }

    @Test
    fun swipeShoppingItem_deleteItemInDb() {
        val shoppingItem = ShoppingItem("name", 1, 1f, "")
        var testViewModel: ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            testViewModel = viewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<
                    ShoppingItemAdapter.ViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.liveDataShoppingItems?.getOrAwaitValue()).isEmpty()
    }


}