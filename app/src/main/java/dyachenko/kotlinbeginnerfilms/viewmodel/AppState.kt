package dyachenko.kotlinbeginnerfilms.viewmodel

import dyachenko.kotlinbeginnerfilms.model.Film

sealed class AppState {
    data class Success(val filmData: Film) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
