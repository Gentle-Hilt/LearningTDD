package gentle.hilt.learnunittests2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import gentle.hilt.learnunittests2.db.ShoppingDao
import gentle.hilt.learnunittests2.db.ShoppingItem
import gentle.hilt.learnunittests2.others.Resource
import gentle.hilt.learnunittests2.remote.PixabayApi
import gentle.hilt.learnunittests2.remote.reponses.ImageResponse


import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)

    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()

    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayApi.searchForImages(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("An unknown error occurred")
            } else {
                Resource.Error("An unknown error occurred")
            }
        } catch (e: Exception) {
            Log.e("EXCEPTION", "EXCEPTION:", e)
            Resource.Error("Couldn't reach the server. Check your internet connection")
        }
    }
}