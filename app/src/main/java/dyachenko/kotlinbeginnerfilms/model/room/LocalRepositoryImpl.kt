package dyachenko.kotlinbeginnerfilms.model.room

import java.util.*

class LocalRepositoryImpl(
    private val localDataSource: HistoryDao
) : LocalRepository {

    override fun getAllHistory(): List<HistoryEntity> {
        return localDataSource.all()
    }

    override fun saveHistory(filmId: Int, filmTitle: String) {
        localDataSource.insert(HistoryEntity(0, filmId, filmTitle, Date()))
    }
}