package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.getSeconds
import dyachenko.kotlinbeginnerfilms.model.Repository
import dyachenko.kotlinbeginnerfilms.model.RepositoryImpl
import java.lang.Thread.sleep
import java.util.*

class FilmsViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getFilmFromLocalSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(SLEEP_MILLIS)
            if (Calendar.getInstance().getSeconds() % DIVIDER == REMAINDER) {
                liveDataToObserve.postValue(AppState.Error(Exception()))
            } else {
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getFilmsFromLocalStorage()))
            }
        }.start()
    }

    companion object {
        private const val SLEEP_MILLIS = 1_000L
        private const val DIVIDER = 2
        private const val REMAINDER = 0
    }
}