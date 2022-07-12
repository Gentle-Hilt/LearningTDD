package gentle.hilt.learnunittests2.remote


import gentle.hilt.learnunittests2.BuildConfig
import gentle.hilt.learnunittests2.remote.reponses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET("/api/")
    suspend fun searchForImages(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.PIXABABY_API
    ): Response<ImageResponse>
}