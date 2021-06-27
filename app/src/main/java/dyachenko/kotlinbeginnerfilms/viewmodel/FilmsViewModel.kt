package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.model.Repository
import dyachenko.kotlinbeginnerfilms.model.RepositoryImpl

class FilmsViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repositoryImpl: Repository = RepositoryImpl()

    fun getLiveData() = liveDataToObserve

    fun getFilmsFromLocalSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(AppState.Success(repositoryImpl.getFilmsFromLocalStorage()))
        }.start()
    }
}