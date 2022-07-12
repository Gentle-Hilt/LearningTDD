package gentle.hilt.learnunittests2.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [ShoppingItem::class],
    version = 1

)
abstract class ShoppingDb : RoomDatabase(){

    abstract fun shoppingDao(): ShoppingDao
}