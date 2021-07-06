package dyachenko.kotlinbeginnerfilms.viewmodel

import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.room.HistoryEntity

sealed class AppState {
    data class Success(val films: List<Film>) : AppState()
    data class SuccessHistory(val historyEntities: List<HistoryEntity>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
