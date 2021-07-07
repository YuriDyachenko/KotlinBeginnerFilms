package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.app.App.Companion.getHistoryDao
import dyachenko.kotlinbeginnerfilms.app.App.Companion.getNoteDao
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepository
import dyachenko.kotlinbeginnerfilms.model.room.LocalRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val historyRepository: LocalRepository =
        LocalRepositoryImpl(getHistoryDao(), getNoteDao())

    fun saveNoteToDB(filmId: Int?, filmTitle: String?, note: String?) {
        if (filmId != null && filmTitle != null && note != null) {
            launch(Dispatchers.IO) {
                historyRepository.saveNote(filmId, filmTitle, note)
            }
        }
    }
}