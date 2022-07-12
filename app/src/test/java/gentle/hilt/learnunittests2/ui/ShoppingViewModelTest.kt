package gentle.hilt.learnunittests2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gentle.hilt.learnunittests2.MainCoroutineRule
import gentle.hilt.learnunittests2.getOrAwaitValue
import gentle.hilt.learnunittests2.others.Constants
import gentle.hilt.learnunittests2.others.Status
import gentle.hilt.learnunittests2.repository.FakeDefaultShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {

        viewModel = ShoppingViewModel(FakeDefaultShoppingRepository())
    }

    @Test
    fun insertEmptyItemIntoDb_returnError() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun insertTooLongNameIntoDb_returnError() {
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(name, "1", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertTooHighAmountIntoDb_returnError() {
        viewModel.insertShoppingItem("name", "99999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertTooHighPriceIntoDb_returnError() {
        viewModel.insertShoppingItem("name", "1", "99999999999999")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertValidInputIntoDb_returnSuccess() {
        viewModel.insertShoppingItem("name", "2", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()


        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


    @Test
    fun setCurrentImageUrlToEmptyBodyAfterAnItemInsertionWasSuccessful_returnEmptyBody() {

        viewModel.insertShoppingItem("name", "1", "3.0")


        val value = viewModel.currentImageUrl.getOrAwaitValue()

        assertThat(value).isEqualTo("")

    }

    @Test
    fun callFunctionWithAnyUrlBeAbleToObserveWithLiveData_returnAnyUrl() {

        viewModel.setCurrentImageUrl("url")

        val value = viewModel.currentImageUrl.getOrAwaitValue()

        assertThat(value).isEqualTo("url")

    }
}