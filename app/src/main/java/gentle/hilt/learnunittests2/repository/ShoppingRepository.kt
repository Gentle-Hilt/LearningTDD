package gentle.hilt.learnunittests2.repository

import androidx.lifecycle.LiveData
import gentle.hilt.learnunittests2.db.ShoppingItem
import gentle.hilt.learnunittests2.others.Resource
import gentle.hilt.learnunittests2.remote.reponses.ImageResponse



interface ShoppingRepository  {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}