package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.model.Repository
import dyachenko.kotlinbeginnerfilms.model.RepositoryImpl
import java.lang.Thread.sleep
import java.util.*

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getFilmFromLocalSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(3000)
            if (Calendar.getInstance().get(Calendar.SECOND) % 2 == 0) {
                liveDataToObserve.postValue(AppState.Error(Exception()))
            } else {
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getFilmFromLocalStoage()))
            }
        }.start()
    }

}