package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dyachenko.kotlinbeginnerfilms.getLines
import dyachenko.kotlinbeginnerfilms.model.Film
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class FilmViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve

    fun getFilmFromServer(filmId: Int) = getDataFromServer(filmId)

    private fun getDataFromServer(filmId: Int) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            try {
                val uri = URL("$SITE$filmId?api_key=$API_KEY&language=ru-RU")
                lateinit var connection: HttpsURLConnection
                try {
                    connection = uri.openConnection() as HttpsURLConnection
                    connection.requestMethod = GET_METHOD
                    connection.readTimeout = TIMEOUT
                    val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                    val film = Gson().fromJson(bufferedReader.getLines(), Film::class.java)
                    liveDataToObserve.postValue(AppState.Success(listOf(film)))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    liveDataToObserve.postValue(AppState.Error(Exception("Bad URL ($e)")))
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    liveDataToObserve.postValue(AppState.Error(Exception("Unknown HOST ($e)")))
                } catch (e: Exception) {
                    e.printStackTrace()
                    liveDataToObserve.postValue(AppState.Error(e))
                } finally {
                    connection.disconnect()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                liveDataToObserve.postValue(AppState.Error(e))
            }
        }.start()
    }

    companion object {
        const val SITE = "https://api.themoviedb.org/3/movie/"
        const val API_KEY = "9c8d9086c4cce7dfcd52f5455412fa56"
        const val GET_METHOD = "GET"
        const val TIMEOUT = 1_000
    }
}