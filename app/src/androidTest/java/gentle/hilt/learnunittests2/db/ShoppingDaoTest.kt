package gentle.hilt.learnunittests2.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gentle.hilt.learnunittests2.getOrAwaitValue
import gentle.hilt.learnunittests2.launchFragmentInHiltContainer
import gentle.hilt.learnunittests2.ui.fragments.ShoppingFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Before
import org.junit.Rule

import org.junit.Test

import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingDb

    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        hiltRule.inject()
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingDb::class.java
        ).allowMainThreadQueries().build()*/

        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItem).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItem).isEmpty()
    }

    @Test
    fun observeTotalPrice() = runTest {
        val shoppingItem1 = ShoppingItem("name1", 1, 1f, "url", 1)
        val shoppingItem2 = ShoppingItem("name2", 2, 2f, "url2", 2)
        val shoppingItem3 = ShoppingItem("name3", 3, 3f, "url3", 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(1 * 1f + 2 * 2f + 3 * 3f)
    }


    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }
}