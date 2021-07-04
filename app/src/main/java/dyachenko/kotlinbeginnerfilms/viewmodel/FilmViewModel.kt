package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var resourceProvider: ResourceProvider? = null

    private val callback = object : Callback<Film> {
        override fun onResponse(call: Call<Film>, response: Response<Film>) {
            val film = response.body()
            liveDataToObserve.value = if (response.isSuccessful && film != null) {
                AppState.Success(listOf(film))
            } else {
                AppState.Error(Throwable(getString(R.string.error_server_msg)))
            }
        }

        override fun onFailure(call: Call<Film>, t: Throwable) {
            liveDataToObserve.value = AppState.Error(
                Throwable(t.message ?: getString(R.string.error_request_msg))
            )
        }
    }

    fun getLiveData() = liveDataToObserve

    fun getFilmFromServer(filmId: Int) {
        liveDataToObserve.value = AppState.Loading
        RemoteDataSource().getFilm(filmId, callback)
    }

    private fun getString(id: Int) = resourceProvider?.getString(id)
}