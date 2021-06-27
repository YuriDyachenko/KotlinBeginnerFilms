package dyachenko.kotlinbeginnerfilms.model

interface Repository {
    fun getFilmFromServer(id: Int): Film
    fun getFilmsFromLocalStorage(): List<Film>
}