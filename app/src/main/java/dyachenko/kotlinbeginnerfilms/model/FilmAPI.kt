package dyachenko.kotlinbeginnerfilms.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmAPI {
    @GET("3/movie/{id}")
    fun getFilm(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<Film>
}