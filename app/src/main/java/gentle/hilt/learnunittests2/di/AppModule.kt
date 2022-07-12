package gentle.hilt.learnunittests2.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gentle.hilt.learnunittests2.R
import gentle.hilt.learnunittests2.db.ShoppingDao
import gentle.hilt.learnunittests2.db.ShoppingDb
import gentle.hilt.learnunittests2.others.Constants.BASE_URL
import gentle.hilt.learnunittests2.others.Constants.DATABASE_NAME
import gentle.hilt.learnunittests2.remote.PixabayApi
import gentle.hilt.learnunittests2.repository.DefaultShoppingRepository
import gentle.hilt.learnunittests2.repository.ShoppingRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDb(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingDb::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingDb
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi():PixabayApi{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun provideShoppingRepository(
        dao: ShoppingDao,
        api: PixabayApi
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )
}