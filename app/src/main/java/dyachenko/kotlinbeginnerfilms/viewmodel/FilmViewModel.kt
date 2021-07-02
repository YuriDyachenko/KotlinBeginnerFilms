package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.model.FilmLoader

class FilmViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve

    fun getFilmFromServer(filmId: Int) = with(liveDataToObserve) {
        value = AppState.Loading
        Thread {
            try {
                postValue(AppState.Success(listOf(FilmLoader.loadFilm(filmId))))
            } catch (e: Exception) {
                e.printStackTrace()
                postValue(AppState.Error(e))
            }
        }.start()
    }

}