package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.app.App
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepository
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepositoryImpl
import kotlinx.coroutines.*

class HistoryViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val historyRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())

    fun getLiveData() = liveDataToObserve

    fun getAllHistory() {
        liveDataToObserve.value = AppState.Loading
        launch {
            val job = async(Dispatchers.IO) { historyRepository.getAllHistory() }
            liveDataToObserve.value = AppState.SuccessHistory(job.await())
        }
    }
}