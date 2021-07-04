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

    private val pageApi = Retrofit.Builder()
        .baseUrl(SITE)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()
        .create(PageAPI::class.java)

    fun getFilm(id: Int, callback: Callback<Film>) {
        filmApi.getFilm(id, API_KEY, RU_LANG).enqueue(callback)
    }

    fun getFirstPage(callback: Callback<PageDTO>) {
        pageApi.getFirstPage(API_KEY, RU_LANG).enqueue(callback)
    }

    fun getNextPage(page: Int, callback: Callback<PageDTO>) {
        pageApi.getNextPage(API_KEY, RU_LANG, page).enqueue(callback)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val MAX_PAGE = 5
        const val IMAGE_SITE = "https://image.tmdb.org/t/p/w500"
        private const val SITE = "https://api.themoviedb.org/"
        private const val API_KEY = "9c8d9086c4cce7dfcd52f5455412fa56"
        private const val RU_LANG = "ru-RU"
    }
}