package dyachenko.kotlinbeginnerfilms.model

import com.google.gson.GsonBuilder
import dyachenko.kotlinbeginnerfilms.BuildConfig.FILM_API_KEY
import dyachenko.kotlinbeginnerfilms.view.settings.Settings
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
        filmApi.getFilm(id, FILM_API_KEY, RU_LANG).enqueue(callback)
    }

    fun getFirstPage(callback: Callback<PageDTO>) {
        pageApi.getFirstPage(getType(), FILM_API_KEY, RU_LANG).enqueue(callback)
    }

    fun getNextPage(page: Int, callback: Callback<PageDTO>) {
        pageApi.getNextPage(getType(), FILM_API_KEY, RU_LANG, page).enqueue(callback)
    }

    private fun getType() = Settings.FILMS_LIST_TYPE.apiType

    companion object {
        const val FIRST_PAGE = 1
        const val MAX_PAGE = 5
        const val IMAGE_SITE = "https://image.tmdb.org/t/p/w500"
        const val TYPE_POPULAR = "popular"
        const val TYPE_TOP_RATED = "top_rated"
        const val TYPE_NOW_PLAYING = "now_playing"
        const val TYPE_UPCOMING = "upcoming"
        private const val SITE = "https://api.themoviedb.org/"
        private const val RU_LANG = "ru"
    }
}