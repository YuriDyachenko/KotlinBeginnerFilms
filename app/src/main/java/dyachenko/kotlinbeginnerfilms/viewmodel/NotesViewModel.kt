package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.app.App.Companion.getHistoryDao
import dyachenko.kotlinbeginnerfilms.app.App.Companion.getNoteDao
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepository
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepositoryImpl
import kotlinx.coroutines.*

class NotesViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val historyRepository: LocalRepository =
        LocalRepositoryImpl(getHistoryDao(), getNoteDao())

    fun getLiveData() = liveDataToObserve

    fun getNotesByFilmId(filmId: Int) {
        liveDataToObserve.value = AppState.Loading
        launch {
            val job = async(Dispatchers.IO) { historyRepository.getNotesByFilmId(filmId) }
            liveDataToObserve.value = AppState.SuccessNotes(job.await())
        }
    }
}