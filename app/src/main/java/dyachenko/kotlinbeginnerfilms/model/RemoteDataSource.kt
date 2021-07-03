package dyachenko.kotlinbeginnerfilms.model

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private val filmApi = Retrofit.Builder()
        .baseUrl(SITE)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()
        .create(FilmAPI::class.java)

    fun getFilm(id: Int, callback: Callback<Film>) {
        filmApi.getFilm(id, API_KEY, RU_LANG).enqueue(callback)
    }

    companion object {
        private const val SITE = "https://api.themoviedb.org/"
        private const val API_KEY = "9c8d9086c4cce7dfcd52f5455412fa56"
        private const val RU_LANG = "ru-RU"
    }
}