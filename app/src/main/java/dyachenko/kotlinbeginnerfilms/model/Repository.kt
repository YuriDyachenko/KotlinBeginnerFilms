package dyachenko.kotlinbeginnerfilms.model

interface Repository {
    fun getFilmFromServer(): Film
    fun getFilmsFromLocalStorage(): List<Film>
}