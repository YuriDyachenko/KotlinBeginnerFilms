package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.model.FilmLoader

class FilmsViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve

    fun getFilmsFromServer() = with(liveDataToObserve) {
        value = AppState.Loading
        Thread {
            try {
                postValue(AppState.Success(FilmLoader.loadFilms()))
            } catch (e: Exception) {
                e.printStackTrace()
                postValue(AppState.Error(e))
            }
        }.start()
    }
}