package dyachenko.kotlinbeginnerfilms.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PageAPI {
    @GET("3/movie/{type}")
    fun getFirstPage(
        @Path("type") type: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<PageDTO>

    @GET("3/movie/{type}")
    fun getNextPage(
        @Path("type") type: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<PageDTO>
}