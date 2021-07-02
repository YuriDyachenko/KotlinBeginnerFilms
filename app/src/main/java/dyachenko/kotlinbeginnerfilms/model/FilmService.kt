package dyachenko.kotlinbeginnerfilms.model

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dyachenko.kotlinbeginnerfilms.view.details.FilmFragment.Companion.NO_ID

const val FILM_INTENT_FILTER = "FILM_INTENT_FILTER"
const val FILM_ID_EXTRA = "FILM_ID_EXTRA"
const val FILM_EXTRA = "FILM_EXTRA"
const val LOAD_RESULT_EXTRA = "LOAD_RESULT_EXTRA"
const val LOAD_RESULT_DESCRIPTION_EXTRA = "LOAD_RESULT_DESCRIPTION_EXTRA"
const val LOAD_RESULT_OK = "OK"
const val LOAD_RESULT_ERROR = "ERROR"
const val EMPTY_EXTRA_DESCRIPTION = "DATA IS EMPTY"
const val EMPTY_INTENT_DESCRIPTION = "INTENT IS EMPTY"
const val EMPTY_ERROR_DESCRIPTION = "EMPTY ERROR"

class FilmService(name: String = "FilmService") : IntentService(name) {

    private val broadcastIntent = Intent(FILM_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val filmId = it.getIntExtra(FILM_ID_EXTRA, NO_ID)
            if (filmId == NO_ID) {
                putResult(LOAD_RESULT_ERROR, EMPTY_EXTRA_DESCRIPTION)
            } else {
                try {
                    val film = FilmLoader.loadFilm(filmId)
                    broadcastIntent.putExtra(FILM_EXTRA, film)
                    putResult(LOAD_RESULT_OK)
                } catch (e: Exception) {
                    putResult(LOAD_RESULT_ERROR, e.message ?: EMPTY_ERROR_DESCRIPTION)
                }
            }
        } ?: putResult(LOAD_RESULT_ERROR, EMPTY_INTENT_DESCRIPTION)
    }

    private fun putResult(result: String, description: String = "") {
        broadcastIntent.putExtra(LOAD_RESULT_EXTRA, result)
        broadcastIntent.putExtra(LOAD_RESULT_DESCRIPTION_EXTRA, description)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}