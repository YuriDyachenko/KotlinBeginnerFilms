package dyachenko.kotlinbeginnerfilms.model.room

interface LocalRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveHistory(filmId: Int, filmTitle: String)
}