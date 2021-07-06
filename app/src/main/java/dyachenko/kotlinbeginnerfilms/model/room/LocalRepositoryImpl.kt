package dyachenko.kotlinbeginnerfilms.model.room

import java.util.*

class LocalRepositoryImpl(
    private val localDataSourceHistory: HistoryDao,
    private val localDataSourceNote: NoteDao
) : LocalRepository {

    override fun getAllHistory(): List<HistoryEntity> {
        return localDataSourceHistory.all()
    }

    override fun saveHistory(filmId: Int, filmTitle: String) {
        localDataSourceHistory.insert(HistoryEntity(0, filmId, filmTitle, Date()))
    }

    override fun getAllNotes(): List<NoteEntity> {
        return localDataSourceNote.all()
    }

    override fun getNotesByFilmId(filmId: Int): List<NoteEntity> {
        return localDataSourceNote.getDataByFilmId(filmId)
    }

    override fun saveNote(filmId: Int, filmTitle: String, note: String) {
        localDataSourceNote.insert(NoteEntity(0, filmId, filmTitle, note, Date()))
    }
}