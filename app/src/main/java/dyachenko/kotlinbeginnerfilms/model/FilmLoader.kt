package dyachenko.kotlinbeginnerfilms.model

import com.google.gson.Gson
import dyachenko.kotlinbeginnerfilms.getLines
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection
import kotlin.math.min

object FilmLoader {
    const val IMAGE_SITE = "https://image.tmdb.org/t/p/w500"
    private const val SITE = "https://api.themoviedb.org/3/movie/"
    private const val API_KEY = "9c8d9086c4cce7dfcd52f5455412fa56"
    private const val GET_METHOD = "GET"
    private const val TIMEOUT = 1_000
    private const val GET_FILM = "${SITE}%d?api_key=${API_KEY}&language=%s"
    private const val GET_FILMS = "${SITE}popular?api_key=${API_KEY}&language=%s"
    private const val GET_FILMS_PAGE = "${GET_FILMS}&page=%d"
    private const val NO_PAGES = 0
    private const val SECOND_PAGE = 2
    private const val MAX_PAGES = 5
    private const val RU_LANG = "ru-RU"
    private const val EN_LANG = "en-US"
    private var isEnLang = false
    private var currentLang = RU_LANG

    fun changeLang() {
        isEnLang = !isEnLang
        currentLang = if (isEnLang) EN_LANG else RU_LANG
    }

    private fun load(cmd: String): String {
        try {
            val uri = URL(cmd)
            lateinit var connection: HttpsURLConnection
            try {
                connection = uri.openConnection() as HttpsURLConnection
                connection.requestMethod = GET_METHOD
                connection.readTimeout = TIMEOUT
                val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                return bufferedReader.getLines()
            } catch (e: FileNotFoundException) {
                throw Exception("Bad URL ($e)", e)
            } catch (e: UnknownHostException) {
                throw Exception("Unknown HOST ($e)", e)
            } catch (e: Exception) {
                throw e
            } finally {
                connection.disconnect()
            }
        } catch (e: MalformedURLException) {
            throw e
        }
    }

    fun loadFilm(id: Int): Film {
        val cmd = String.format(GET_FILM, id, currentLang)
        return Gson().fromJson(load(cmd), Film::class.java)
    }

    fun loadFilms(): List<Film> {
        val page = Gson().fromJson(load(String.format(GET_FILMS, currentLang)), PageDTO::class.java)
        val list = page.results.toMutableList()
        val maxPageIndex = min(page.total_pages ?: NO_PAGES, MAX_PAGES)
        for (pageIndex in SECOND_PAGE..maxPageIndex) {
            val cmd = String.format(GET_FILMS_PAGE, currentLang, pageIndex)
            val nextPage = Gson().fromJson(load(cmd), PageDTO::class.java)
            list.addAll(nextPage.results.toList())
        }
        return list
    }

}