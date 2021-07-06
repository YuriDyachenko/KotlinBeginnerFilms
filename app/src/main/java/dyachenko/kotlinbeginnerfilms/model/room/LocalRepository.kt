package dyachenko.kotlinbeginnerfilms.model.room

interface LocalRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveHistory(filmId: Int, filmTitle: String)
    fun getAllNotes(): List<NoteEntity>
    fun getNotesByFilmId(filmId: Int): List<NoteEntity>
    fun saveNote(filmId: Int, filmTitle: String, note: String)
}